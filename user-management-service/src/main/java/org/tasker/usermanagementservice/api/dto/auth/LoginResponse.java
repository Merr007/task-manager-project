package org.tasker.usermanagementservice.api.dto.auth;

public record LoginResponse(String token, String refreshToken) {
}
