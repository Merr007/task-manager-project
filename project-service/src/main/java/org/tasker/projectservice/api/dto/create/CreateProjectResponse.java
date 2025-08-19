package org.tasker.projectservice.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response for project creation")
public record CreateProjectResponse(@Schema(description = "Identifier of created project") Long projectId) {
}
