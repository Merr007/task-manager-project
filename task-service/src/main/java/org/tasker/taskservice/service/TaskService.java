package org.tasker.taskservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.tasker.taskservice.api.dto.TaskUpdateDto;
import org.tasker.taskservice.api.dto.TaskCreateDto;
import org.tasker.taskservice.data.entities.Task;
import org.tasker.taskservice.domain.TaskStatus;

import java.util.UUID;

public interface TaskService {
    Task createTask(TaskCreateDto request);
    Task getTaskById(UUID id);
    Page<Task> getTasksBySimpleFilter(UUID projectId, UUID assigneeId, TaskStatus status, Pageable pageable);
    Task updateTask(UUID id, TaskUpdateDto patch);
    Task assignTask(UUID id, UUID assigneeId);
    Task changeStatus(UUID id, TaskStatus status);
    void deleteTask(UUID id);
}
