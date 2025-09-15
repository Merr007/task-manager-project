package org.tasker.usermanagementservice.service;

import org.tasker.common.api.dto.user.GetUserRequest;
import org.tasker.common.api.dto.user.GetUserResponse;

import java.util.List;

public interface UserService {

    GetUserResponse getUserById(String userId);

    GetUserResponse getUserByUsername(String username);

    List<GetUserResponse> getBatchUsers(List<GetUserRequest> users);
}
