package org.tasker.usermanagementservice.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RefreshTokenRequest",
        description = "Request object for refreshing access token"
)
public record RefreshTokenRequest(
        @Schema(description = "JWT refresh token used to obtain a new access token")
        String refreshToken) {
}
