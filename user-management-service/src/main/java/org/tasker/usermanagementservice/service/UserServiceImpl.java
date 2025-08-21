package org.tasker.usermanagementservice.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.tasker.usermanagementservice.exception.UserNotFoundException;
import org.tasker.usermanagementservice.web.dto.user.GetUserInfoResponse;

@Service
public class UserServiceImpl implements UserService {

    private final KeycloakService keycloakService;

    public UserServiceImpl(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @Override
    public GetUserInfoResponse getUserInfo(String userId) throws UserNotFoundException {
        UserRepresentation user = keycloakService.getUser(userId);
        return new GetUserInfoResponse(user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail());
    }
}
