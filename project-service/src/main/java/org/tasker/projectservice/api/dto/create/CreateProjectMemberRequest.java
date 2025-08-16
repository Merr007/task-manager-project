package org.tasker.projectservice.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.tasker.projectservice.domain.model.ProjectRole;

@Schema(description = "Project member of created project")
public record CreateProjectMemberRequest(
        @NotBlank
        @Schema(description = "User identifier")
        Long userId,

        @Schema(description = "User role in project")
        ProjectRole role
) {}
