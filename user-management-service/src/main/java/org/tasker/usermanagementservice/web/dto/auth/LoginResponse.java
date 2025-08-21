package org.tasker.usermanagementservice.web.dto.auth;

public record LoginResponse(String token, String refreshToken) {
}
