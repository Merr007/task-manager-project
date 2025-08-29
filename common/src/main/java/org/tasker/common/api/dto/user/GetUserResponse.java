package org.tasker.common.api.dto.user;

public record GetUserResponse(
        String id,
        String username,
        String firstName,
        String lastName,
        String email) {
}