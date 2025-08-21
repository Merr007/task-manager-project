package org.tasker.usermanagementservice.web.dto.user;

public record GetUserInfoResponse(
        String username,
        String firstName,
        String lastName,
        String email) {
}