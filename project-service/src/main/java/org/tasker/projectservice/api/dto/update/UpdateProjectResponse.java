package org.tasker.projectservice.api.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tasker.projectservice.domain.model.ProjectStatus;

import java.time.LocalDate;

public record UpdateProjectResponse(
        @Schema(description = "Project name")
        String projectName,

        @Schema(description = "Project description")
        String description,

        @Schema(description = "Project status")
        ProjectStatus status,

        @Schema(description = "Project start date", example = "2024-01-01")
        LocalDate startDate,

        @Schema(description = "Project end date", example = "2024-01-01")
        LocalDate endDate
) {
}
