package org.tasker.usermanagementservice.service;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tasker.usermanagementservice.exception.*;
import org.tasker.usermanagementservice.model.UserRole;
import org.tasker.usermanagementservice.security.utils.KeycloakAuthFactory;
import org.tasker.usermanagementservice.security.utils.UserSecurityUtils;
import org.tasker.usermanagementservice.web.dto.auth.*;

import java.util.Collections;

@Service
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    private final Keycloak keycloak;
    private final KeycloakAuthFactory authFactory;

    @Value("${keycloak.realm}")
    private String realmName;

    public KeycloakServiceImpl(Keycloak keycloak, KeycloakAuthFactory loginFactory) {
        this.keycloak = keycloak;
        this.authFactory = loginFactory;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) throws InvalidCredentialsException {
        try (Keycloak keycloakLogin = authFactory.of(loginRequest)) {
            AccessTokenResponse accessTokenResponse = keycloakLogin.tokenManager().getAccessToken();
            return new LoginResponse(accessTokenResponse.getToken(), accessTokenResponse.getRefreshToken());
        } catch (Exception e) {
            log.error("Login failed for user: {}", loginRequest.username());
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

    @Override
    public String registerUser(RegisterRequest registerRequest) throws AlreadyExistsException, KeycloakRegistrationException {

        UserRepresentation user = getUserRepresentation(registerRequest);
        Response response = keycloak.realm(realmName).users().create(user);

        if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
            log.warn("User registration failed: username already exists: {}", registerRequest.username());
            throw new AlreadyExistsException("User with username " + registerRequest.username() + " already exists");
        }

        String userId;
        try {
            userId = CreatedResponseUtil.getCreatedId(response);
            assignRoles(userId, UserSecurityUtils.generateProviderRoles(UserRole.USER));
            log.info("User registered successfully with userId: {}", userId);
        } catch (WebApplicationException e) {
            throw new KeycloakRegistrationException("Keycloak registration failed");
        }

        return userId;
    }

    @Override
    public UserRepresentation getUser(String id) throws NotFoundException {
        try {
            return keycloak.realm(realmName).users().get(id).toRepresentation();
        } catch (NotFoundException e) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try (Response response = authFactory.ofRefresh(refreshTokenRequest.refreshToken())) {
            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                AccessTokenResponse accessTokenResponse = response.readEntity(AccessTokenResponse.class);
                return new RefreshTokenResponse(accessTokenResponse.getToken(),
                        accessTokenResponse.getRefreshToken());
            } else {
                throw new RefreshTokenExpirationException("Refresh token has been expired. Please login.");
            }
        }
    }


    private UserRepresentation getUserRepresentation(RegisterRequest registerRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerRequest.password());
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

    private void assignRoles(String userId, String role) {
        RoleRepresentation roleRepresentation = keycloak.realm(realmName).roles().get(role).toRepresentation();
        keycloak.realm(realmName)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Collections.singletonList(roleRepresentation));
    }
}
