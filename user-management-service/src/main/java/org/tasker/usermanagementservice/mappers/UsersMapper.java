package org.tasker.usermanagementservice.mappers;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.tasker.common.api.dto.user.GetUserRequest;
import org.tasker.common.api.dto.user.GetUserResponse;
import org.tasker.common.grpc.GetGrpcUserRequest;
import org.tasker.common.grpc.GetGrpcUserResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    GetUserResponse toGetUserResponse(UserRepresentation user);

    default List<GetUserRequest> toGetUserRequest(List<GetGrpcUserRequest> getGrpcUserRequest) {
        return getGrpcUserRequest.stream()
                .map(request -> new GetUserRequest(request.getUserId()))
                .toList();
    }

    default List<GetGrpcUserResponse> toGetGrpcUsersResponse(List<GetUserResponse> getUserResponses) {
        return getUserResponses.stream()
                .map(this::toGetGrpcUserResponse)
                .toList();
    }

    default GetGrpcUserResponse toGetGrpcUserResponse(GetUserResponse getUserResponse) {
        return GetGrpcUserResponse.newBuilder()
                .setUserId(getUserResponse.id())
                .setUsername(getUserResponse.username())
                .setFirstName(getUserResponse.firstName())
                .setLastName(getUserResponse.lastName())
                .setEmail(getUserResponse.email())
                .build();
    }
}
