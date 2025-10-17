package org.tasker.taskservice.api.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import org.tasker.taskservice.domain.model.TaskPriority;
import org.tasker.taskservice.domain.model.TaskStatus;

import java.time.LocalDateTime;

@Schema(description = "Task update request")
public record UpdateTaskRequest(
        @Size(min = 1, message = "Title must be at least 1 character long")
        @Schema(description = "Task title")
        String title,

        @Schema(description = "Task description")
        String description,

        @Schema(description = "Task status")
        TaskStatus status,

        @Schema(description = "Task priority")
        TaskPriority priority,

        @Schema(description = "Planned completion date")
        LocalDateTime dueDate,

        @Schema(description = "Actual completion date")
        LocalDateTime completionDate
){
}
