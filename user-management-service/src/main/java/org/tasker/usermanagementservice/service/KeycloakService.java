package org.tasker.usermanagementservice.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.tasker.usermanagementservice.web.dto.auth.*;

import java.util.List;

public interface KeycloakService {

    LoginResponse login(LoginRequest loginRequest);

    String registerUser(RegisterRequest registerRequest);

    UserRepresentation getUserById(String id);

    UserRepresentation getUserByUsername(String username);

    List<UserRepresentation> getUsersById(List<String> ids);

    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
