package org.tasker.projectservice.api.dto.get;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tasker.projectservice.domain.model.ProjectStatus;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Get project info")
public record GetProjectResponse(
        @Schema(description = "Project id")
        Long id,

        @Schema(description = "Project name")
        String name,

        @Schema(description = "Project description")
        String description,

        @Schema(description = "Project status")
        ProjectStatus status,

        @Schema(description = "Project start date")
        LocalDate startDate,

        @Schema(description = "Project end date")
        LocalDate endDate,

        @Schema(description = "Project tags")
        Set<GetProjectTag> tags
) {
}