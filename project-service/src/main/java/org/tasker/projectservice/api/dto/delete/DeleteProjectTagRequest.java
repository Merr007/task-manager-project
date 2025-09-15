package org.tasker.projectservice.api.dto.delete;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Delete project tag")
public record DeleteProjectTagRequest(
        @NotNull
        @Schema(description = "Tag name")
        String name
) {
}
