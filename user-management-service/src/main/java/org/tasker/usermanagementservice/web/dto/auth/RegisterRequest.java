package org.tasker.usermanagementservice.web.dto.auth;

public record RegisterRequest(
        String username,
        String password,
        String email,
        String firstName,
        String lastName) {
}