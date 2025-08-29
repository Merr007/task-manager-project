package org.tasker.usermanagementservice.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "LoginRequest",
        description = "Request object for user authentication",
        example = """
        {
            "username": "john_doe",
            "password": "SecurePassword123!"
        }
        """
)
public record LoginRequest(
        @Schema(description = "Username or email address for authentication")
        @NotBlank(message = "Username is required")
        String username,

        @Schema(description = "User password for authentication")
        @NotBlank(message = "Password is required")
        String password) {
}
