package org.tasker.taskservice.api.dto;


import org.tasker.taskservice.domain.TaskStatus;
import org.tasker.taskservice.domain.TaskPriority;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class TaskCreateDto {
    @NotNull
    private UUID projectId;

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 20_000)
    private String description;

    @NotNull
    private TaskPriority priority;

    private UUID assigneeId;
    private UUID reporterId;
    private OffsetDateTime dueAt;

    private TaskStatus status;
}
