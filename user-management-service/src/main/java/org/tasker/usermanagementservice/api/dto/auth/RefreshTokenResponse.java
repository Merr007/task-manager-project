package org.tasker.usermanagementservice.api.dto.auth;

public record RefreshTokenResponse(String token, String refreshToken) {
}
