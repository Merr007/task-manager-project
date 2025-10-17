package org.tasker.taskservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.tasker.taskservice.api.dto.create.CreateTaskRequest;
import org.tasker.taskservice.api.dto.create.CreateTaskResponse;
import org.tasker.taskservice.api.dto.get.GetTaskCommentResponse;
import org.tasker.taskservice.api.dto.get.GetTaskResponse;
import org.tasker.taskservice.api.dto.update.UpdateTaskResponse;
import org.tasker.taskservice.data.entities.Task;
import org.tasker.taskservice.data.entities.TaskComment;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class TaskMapper {

    @Mapping(target = "status", constant = "TODO")
    public abstract Task toTask(CreateTaskRequest createTaskRequest);

    @Mapping(target = "taskId", source = "id")
    public abstract CreateTaskResponse toCreateTaskResponse(Task task);

    @Mapping(target = "taskId", source = "id")
    public abstract GetTaskResponse toGetTaskResponse(Task task);

    public abstract UpdateTaskResponse toUpdateTaskResponse(Task task);

    @Mapping(target = "commentId", source = "id")
    public abstract GetTaskCommentResponse toGetTaskCommentResponse(TaskComment comment);
}
