package org.tasker.usermanagementservice.web.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasker.common.api.dto.user.GetUserResponse;
import org.tasker.usermanagementservice.service.UserService;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves user information based on the provided user ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            )
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetUserResponse> getUserById(
            @Parameter(description = "Unique identifier of the user", required = true)
            @PathVariable("id") String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(
            summary = "Get user by username",
            description = "Retrieves user information based on the provided username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User found"
            ),
            @ApiResponse(responseCode = "404",
                    description = "User not found"
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping("/byName/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GetUserResponse> getUserByUsername(
            @Parameter(description = "Username of the user", required = true)
            @PathVariable("username") String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
}
