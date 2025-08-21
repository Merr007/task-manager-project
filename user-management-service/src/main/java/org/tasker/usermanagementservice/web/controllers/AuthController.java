package org.tasker.usermanagementservice.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasker.usermanagementservice.exception.AlreadyExistsException;
import org.tasker.usermanagementservice.exception.InvalidCredentialsException;
import org.tasker.usermanagementservice.exception.KeycloakRegistrationException;
import org.tasker.usermanagementservice.service.KeycloakService;
import org.tasker.usermanagementservice.web.dto.auth.LoginRequest;
import org.tasker.usermanagementservice.web.dto.auth.RefreshTokenRequest;
import org.tasker.usermanagementservice.web.dto.auth.RefreshTokenResponse;
import org.tasker.usermanagementservice.web.dto.auth.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KeycloakService keycloakService;

    public AuthController(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            String id = keycloakService.registerUser(registerRequest);
            return ResponseEntity.ok("User registered successfully. UserID: " + id);
        } catch (AlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (KeycloakRegistrationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(keycloakService.login(loginRequest));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not valid credentials");
        }
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(keycloakService.refreshToken(refreshTokenRequest));
    }
}
