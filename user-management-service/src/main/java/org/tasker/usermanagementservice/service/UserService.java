package org.tasker.usermanagementservice.service;

import org.tasker.common.api.dto.user.GetUserResponse;

public interface UserService {

    GetUserResponse getUserById(String userId);

    GetUserResponse getUserByUsername(String username);
}
