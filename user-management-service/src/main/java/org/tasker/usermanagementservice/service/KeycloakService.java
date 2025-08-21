package org.tasker.usermanagementservice.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.tasker.usermanagementservice.web.dto.auth.*;

public interface KeycloakService {

    LoginResponse login(LoginRequest loginRequest);

    String registerUser(RegisterRequest registerRequest);

    UserRepresentation getUser(String id);

    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
