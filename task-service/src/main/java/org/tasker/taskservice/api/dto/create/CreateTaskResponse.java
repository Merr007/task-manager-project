package org.tasker.taskservice.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for task creation")
public record CreateTaskResponse(@Schema(description = "Identifier of created task") Long taskId) {
}
