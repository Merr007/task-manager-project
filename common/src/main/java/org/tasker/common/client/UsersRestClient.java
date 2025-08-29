package org.tasker.common.client;

import org.tasker.common.api.dto.user.GetUserResponse;

import java.util.Optional;

public interface UsersRestClient {

    Optional<GetUserResponse> getUserById(String id);

    Optional<GetUserResponse> getUserByUsername(String username);
}
