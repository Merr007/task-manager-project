package org.tasker.usermanagementservice.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.tasker.usermanagementservice.exception.UserNotFoundException;
import org.tasker.common.api.dto.user.GetUserResponse;

@Service
public class UserServiceImpl implements UserService {

    private final KeycloakService keycloakService;

    public UserServiceImpl(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @Override
    public GetUserResponse getUserById(String userId) throws UserNotFoundException {
        UserRepresentation user = keycloakService.getUserById(userId);
        return new GetUserResponse(
                userId,
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail());
    }

    @Override
    public GetUserResponse getUserByUsername(String username) throws UserNotFoundException {
        UserRepresentation user = keycloakService.getUserByUsername(username);
        return new GetUserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail());
    }
}
