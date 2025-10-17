package org.tasker.projectservice.api.dto.get;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Get project tags")
public record GetProjectTag(
        @Schema(description = "Tag name")
        String name) {}
