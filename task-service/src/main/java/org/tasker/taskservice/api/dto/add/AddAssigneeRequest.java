package org.tasker.taskservice.api.dto.add;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Add task's assignee")
public record AddAssigneeRequest(
        @NotBlank(message = "Assignee Id must be not blank")
        @Schema(description = "Task's assignee id")
        String assigneeId
) {
}
