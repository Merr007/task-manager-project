package org.tasker.common.api.dto.user;

import jakarta.validation.constraints.NotBlank;

public record GetUserRequest(
        @NotBlank
        String id
) {
}
