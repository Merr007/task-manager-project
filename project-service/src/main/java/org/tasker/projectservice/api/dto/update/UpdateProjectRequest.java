package org.tasker.projectservice.api.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UpdateProjectRequest(
        @NotBlank(message = "Project name must be not blank")
        @Schema(description = "Project name")
        String projectName,

        @Schema(description = "Project description")
        String description,

        @Schema(description = "Project start date", example = "2024-01-01")
        LocalDate startDate,

        @Schema(description = "Project end date", example = "2024-01-01")
        LocalDate endDate
) {
}
