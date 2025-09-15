package org.tasker.common.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.tasker.common.api.dto.user.GetUserRequest;
import org.tasker.common.api.dto.user.GetUserResponse;

import java.util.List;
import java.util.Optional;

public class RestClientUsersRestClient implements UsersRestClient {

    private static final String BASE_USERS = "/v1/users";

    private final RestClient restClient;

    private final ParameterizedTypeReference<List<GetUserResponse>> responseType = new ParameterizedTypeReference<>() {
    };

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

    @Override
    public List<GetUserResponse> getBatchUsersById(List<GetUserRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return List.of();
        }
        return restClient
                .post()
                .uri(BASE_USERS)
                .body(requests)
                .retrieve()
                .body(responseType);
    }
}
