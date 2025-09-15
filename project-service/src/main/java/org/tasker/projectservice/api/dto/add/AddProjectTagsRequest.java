package org.tasker.projectservice.api.dto.add;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Set;

@Schema(description = "Add project tags")
public record AddProjectTagsRequest(
        @NotEmpty
        @UniqueElements
        @Schema(description = "Tag names")
        Set<AddProjectTagRequest> tags
) {
}
