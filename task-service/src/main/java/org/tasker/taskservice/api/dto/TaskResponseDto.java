package org.tasker.taskservice.api.dto;

import lombok.Builder;
import lombok.Value;
import org.tasker.taskservice.domain.TaskPriority;
import org.tasker.taskservice.domain.TaskStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class TaskResponseDto {
    UUID id;
    UUID projectId;
    String title;
    String description;
    TaskStatus status;
    TaskPriority priority;
    UUID assigneeId;
    UUID reporterId;
    OffsetDateTime dueAt;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
    long version;
}
