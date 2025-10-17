package org.tasker.projectservice.api.dto.delete;

import jakarta.validation.constraints.NotNull;

public record DeleteProjectMemberRequest(
        @NotNull
        Long memberId) {
}
