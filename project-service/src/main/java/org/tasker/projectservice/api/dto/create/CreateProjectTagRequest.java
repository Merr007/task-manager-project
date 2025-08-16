package org.tasker.projectservice.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Create project tag")
public record CreateProjectTagRequest(
        @NotBlank(message = "Tag must be not blank")
        @Schema(description = "Tag name")
        String name
) {}
