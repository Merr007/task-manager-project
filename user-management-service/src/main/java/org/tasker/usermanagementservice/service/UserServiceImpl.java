package org.tasker.usermanagementservice.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.tasker.common.api.dto.user.GetUserRequest;
import org.tasker.usermanagementservice.exception.UserNotFoundException;
import org.tasker.common.api.dto.user.GetUserResponse;
import org.tasker.usermanagementservice.mappers.UsersMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakService keycloakService;
    private final UsersMapper usersMapper;

    @Override
    public GetUserResponse getUserById(String userId) throws UserNotFoundException {
        UserRepresentation user = keycloakService.getUserById(userId);
        return usersMapper.toGetUserResponse(user);
    }

    @Override
    public GetUserResponse getUserByUsername(String username) throws UserNotFoundException {
        UserRepresentation user = keycloakService.getUserByUsername(username);
        return usersMapper.toGetUserResponse(user);
    }

    @Override
    public List<GetUserResponse> getBatchUsers(List<GetUserRequest> users) {
        List<UserRepresentation> usersRepresentation = keycloakService.getUsersById(users.stream()
                .map(GetUserRequest::id)
                .toList());
        return usersRepresentation.stream()
                .map(usersMapper::toGetUserResponse)
                .toList();
    }
}
