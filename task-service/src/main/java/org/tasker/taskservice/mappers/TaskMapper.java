package org.tasker.taskservice.mappers;

import org.mapstruct.*;
import org.tasker.taskservice.api.dto.TaskCreateDto;
import org.tasker.taskservice.api.dto.TaskResponseDto;
import org.tasker.taskservice.api.dto.TaskUpdateDto;
import org.tasker.taskservice.data.entities.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Task toEntity(TaskCreateDto request);

    TaskResponseDto toResponse(Task entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(@MappingTarget Task target, TaskUpdateDto patch);
}