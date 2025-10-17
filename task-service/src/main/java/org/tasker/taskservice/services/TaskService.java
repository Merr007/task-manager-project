package org.tasker.taskservice.services;

import org.springframework.data.domain.Pageable;
import org.tasker.taskservice.api.dto.add.AddAssigneeRequest;
import org.tasker.taskservice.api.dto.create.CreateTaskRequest;
import org.tasker.taskservice.api.dto.create.CreateTaskResponse;
import org.tasker.taskservice.api.dto.get.GetTaskResponse;
import org.tasker.taskservice.api.dto.update.UpdateTaskRequest;
import org.tasker.taskservice.api.dto.update.UpdateTaskResponse;

import java.util.List;

public interface TaskService {

    CreateTaskResponse createTask(CreateTaskRequest request);

    void addAssignee(Long taskId, AddAssigneeRequest request);

    void deleteTask(Long taskId);

    GetTaskResponse getTask(Long taskId);

    List<GetTaskResponse> getAllTasks(Pageable pageable, Long projectId, String taskTitle);

    UpdateTaskResponse updateTask(Long taskId, UpdateTaskRequest request);
}
