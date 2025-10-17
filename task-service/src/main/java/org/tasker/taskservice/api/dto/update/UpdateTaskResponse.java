package org.tasker.taskservice.api.dto.update;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.tasker.taskservice.domain.model.TaskPriority;
import org.tasker.taskservice.domain.model.TaskStatus;

import java.time.LocalDateTime;

@Schema(description = "Task information response after update")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateTaskResponse(
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
) {
}
