package org.tasker.projectservice.api.dto.add;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Add project tag")
public record AddProjectTagRequest(
        @NotBlank
        @Schema(description = "Tag name")
        String name
) {
}
