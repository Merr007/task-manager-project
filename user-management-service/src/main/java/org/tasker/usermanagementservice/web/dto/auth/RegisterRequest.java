package org.tasker.usermanagementservice.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(
        name = "RegisterRequest",
        description = "Request object for user registration",
        example = """
        {
            "username": "john_doe",
            "password": "SecurePassword123!",
            "email": "john.doe@example.com",
            "firstName": "John",
            "lastName": "Doe"
        }
        """
)
public record RegisterRequest(
        @Schema(description = "Unique username for the new user account")
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Schema(description = "Password for the new user account. Must be at least 6 characters long")
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password,

        @Schema(description = "Valid email address for the new user account")
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @Schema(description = "User's first name")
        @NotBlank(message = "First name is required")
        String firstName,

        @Schema(description = "User's last name")
        @NotBlank(message = "Last name is required")
        String lastName) {
}