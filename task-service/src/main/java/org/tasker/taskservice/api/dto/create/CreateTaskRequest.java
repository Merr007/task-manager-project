package org.tasker.taskservice.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.tasker.taskservice.domain.model.TaskPriority;

@Schema(description = "Create new task request")
public record CreateTaskRequest(
        @NotBlank(message = "Task title must be not blank")
        @Schema(description = "Task title")
        String title,

        @Schema(description = "Task description")
        String description,

        @NotNull(message = "Task priority must be not null")
        @Schema(description = "Task priority", allowableValues = {"LOW", "MEDIUM", "HIGH", "URGENT"})
        TaskPriority priority
) {
}
