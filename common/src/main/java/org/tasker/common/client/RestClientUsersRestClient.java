package org.tasker.common.client;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.tasker.common.api.dto.user.GetUserResponse;

import java.util.Optional;

public class RestClientUsersRestClient implements UsersRestClient {

    private static final String BASE_USERS = "/v1/users";

    private final RestClient restClient;

    public RestClientUsersRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<GetUserResponse> getUserById(String id) throws HttpClientErrorException.NotFound {
        return Optional.ofNullable(restClient
                .get()
                .uri(BASE_USERS + "/{userId}", id)
                .retrieve()
                .body(GetUserResponse.class));
    }

    public Optional<GetUserResponse> getUserByUsername(String username) throws HttpClientErrorException.NotFound {
        return Optional.ofNullable(restClient
                .get()
                .uri(BASE_USERS + "/byName/{username}", username)
                .retrieve()
                .body(GetUserResponse.class));
    }
}
