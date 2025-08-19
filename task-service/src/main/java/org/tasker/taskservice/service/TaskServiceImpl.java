package org.tasker.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.tasker.taskservice.api.dto.TaskUpdateDto;
import org.tasker.taskservice.api.dto.TaskCreateDto;
import org.tasker.taskservice.data.entities.Task;
import org.tasker.taskservice.data.repositories.TaskRepository;
import org.tasker.taskservice.domain.TaskStatus;
import org.tasker.taskservice.events.TaskEvent;
import org.tasker.taskservice.events.TaskEventType;
import org.tasker.taskservice.exceptions.NoSuchTaskException;
import org.tasker.taskservice.mappers.TaskMapper;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final KafkaTemplate<String, TaskEvent> kafka;

    @Value("${TASK_EVENTS_TOPIC:task.events.v1}")
    private String taskEventsTopic;

    @Override
    public Task createTask(TaskCreateDto request) {
        var entity = mapper.toEntity(request);
        if (entity.getStatus() == null) entity.setStatus(TaskStatus.OPEN);
        var saved = repository.save(entity);
        publishAfterCommit(buildEvent(saved, TaskEventType.TASK_CREATED));
        return saved;
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    @Transactional(readOnly = true)
    public Task getTaskById(UUID id) {
        return repository.findById(id).orElseThrow(() -> NoSuchTaskException.task(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Task> getTasksBySimpleFilter(UUID projectId, UUID assigneeId, TaskStatus status, Pageable pageable) {
        if (projectId != null) return repository.findByProjectId(projectId, pageable);
        if (assigneeId != null) return repository.findByAssigneeId(assigneeId, pageable);
        if (status != null) return repository.findByStatus(status, pageable);
        return repository.findAll(pageable);
    }

    @Override
    public Task updateTask(UUID id, TaskUpdateDto patch) {
        var task = getTaskById(id);
        mapper.patch(task, patch);
        var saved = repository.save(task);
        publishAfterCommit(buildEvent(saved, TaskEventType.TASK_UPDATED));
        return saved;
    }

    @Override
    public Task assignTask(UUID id, UUID assigneeId) {
        var task = getTaskById(id);
        task.setAssigneeId(assigneeId);
        var saved = repository.save(task);
        publishAfterCommit(buildEvent(saved, TaskEventType.TASK_ASSIGNED));
        return saved;
    }

    @Override
    public Task changeStatus(UUID id, TaskStatus status) {
        var task = getTaskById(id);
        task.setStatus(status);
        var saved = repository.save(task);
        publishAfterCommit(buildEvent(saved, TaskEventType.TASK_STATUS_CHANGED));
        return saved;
    }

    @Override
    public void deleteTask(UUID id) {
        var existing = getTaskById(id);
        repository.delete(existing);
        publishAfterCommit(TaskEvent.builder()
                .eventId(UUID.randomUUID())
                .type(TaskEventType.TASK_DELETED)
                .taskId(id)
                .occurredAt(OffsetDateTime.now())
                .payload(null)
                .build());
    }

    private TaskEvent buildEvent(Task task, TaskEventType type) {
        return TaskEvent.builder()
                .eventId(UUID.randomUUID())
                .type(type)
                .taskId(task.getId())
                .occurredAt(OffsetDateTime.now())
                .payload(TaskEvent.Payload.builder()
                        .projectId(task.getProjectId())
                        .title(task.getTitle())
                        .description(task.getDescription())
                        .status(task.getStatus())
                        .priority(task.getPriority())
                        .assigneeId(task.getAssigneeId())
                        .reporterId(task.getReporterId())
                        .dueAt(task.getDueAt())
                        .version(task.getVersion())
                        .build())
                .build();
    }

    private void publishAfterCommit(TaskEvent event) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCommit() {
                    kafka.send(taskEventsTopic, event.getTaskId().toString(), event);
                }
            });
        } else {
            kafka.send(taskEventsTopic, event.getTaskId().toString(), event);
        }
    }
}
