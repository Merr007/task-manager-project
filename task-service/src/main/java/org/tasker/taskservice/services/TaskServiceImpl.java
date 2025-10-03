package org.tasker.taskservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasker.common.grpc.exception.GrpcException;
import org.tasker.taskservice.api.dto.add.AddAssigneeRequest;
import org.tasker.taskservice.api.dto.create.CreateTaskRequest;
import org.tasker.taskservice.api.dto.create.CreateTaskResponse;
import org.tasker.taskservice.api.dto.get.GetTaskResponse;
import org.tasker.taskservice.api.dto.update.UpdateTaskRequest;
import org.tasker.taskservice.api.dto.update.UpdateTaskResponse;
import org.tasker.taskservice.data.entities.Task;
import org.tasker.taskservice.data.repositories.TaskRepository;
import org.tasker.taskservice.data.specification.TaskSpecification;
import org.tasker.taskservice.exception.NoSuchMemberInProject;
import org.tasker.taskservice.exception.NoSuchProjectForTaskException;
import org.tasker.taskservice.exception.NoSuchTaskException;
import org.tasker.taskservice.mappers.TaskMapper;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final GrpcClientProjectService grpcClientProjectService;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public CreateTaskResponse createTask(Long projectId, CreateTaskRequest request) throws NoSuchProjectForTaskException {
        if (!grpcClientProjectService.existsByProjectId(projectId)) {
            throw new NoSuchProjectForTaskException("No such project with id " + projectId);
        }

        Task newTask = taskMapper.toTask(request);
        newTask.setProjectId(projectId);

        Task savedTask = taskRepository.save(newTask);

        log.info("Created new task with id " + savedTask.getId());

        return taskMapper.toCreateTaskResponse(savedTask);
    }

    @Override
    @Transactional
    public void addAssignee(Long projectId, Long taskId, AddAssigneeRequest request) throws NoSuchTaskException, GrpcException {
        Task task = taskRepository.findByTaskIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new NoSuchTaskException("No such task with id " + taskId));

        List<String> projectMembers = grpcClientProjectService.getProjectMembers(projectId);

        if (!projectMembers.contains(request.assigneeId())) {
            throw new NoSuchMemberInProject("No such member in project");
        }

        log.info("Adding assignee " + request.assigneeId());

        task.setAssigneeId(request.assigneeId());
    }

    @Override
    @Transactional
    public void deleteTask(Long projectId, Long taskId) throws NoSuchTaskException {
        taskRepository.findByTaskIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new NoSuchTaskException("No such task with id " + taskId));

        taskRepository.deleteById(taskId);

        log.info("Deleted task with id {}", taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public GetTaskResponse getTask(Long projectId, Long taskId) throws NoSuchTaskException {
        Task task = taskRepository.findByTaskIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new NoSuchTaskException("No such task with id " + taskId));

        return taskMapper.toGetTaskResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetTaskResponse> getAllTasks(Pageable pageable, Long projectId, String taskTitle) {
        Specification<Task> spec = TaskSpecification.builder()
                .withProjectId(projectId)
                .withTaskTitleLike(taskTitle)
                .build();
        return taskRepository.findAll(spec, pageable).stream()
                .map(taskMapper::toGetTaskResponse)
                .toList();
    }

    @Override
    @Transactional
    public UpdateTaskResponse updateTask(Long projectId, Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findByTaskIdAndProjectId(taskId, projectId)
                .orElseThrow(() -> new NoSuchTaskException("No such task with id " + taskId));

        if (request.title() != null) {
            task.setTitle(request.title());
        }

        if (request.description() != null) {
            task.setDescription(request.description());
        }

        if (request.status() != null) {
            task.setStatus(request.status());
        }

        if (request.priority() != null) {
            task.setPriority(request.priority());
        }

        if (request.dueDate() != null) {
            task.setDueDate(request.dueDate());
        }

        if (request.completionDate() != null) {
            task.setCompletionDate(request.completionDate());
        }

        return taskMapper.toUpdateTaskResponse(task);
    }
}
