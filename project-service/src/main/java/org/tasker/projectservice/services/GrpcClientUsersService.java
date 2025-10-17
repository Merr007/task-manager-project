package org.tasker.projectservice.services;

import org.tasker.common.grpc.GetGrpcUserResponse;

import java.util.List;
import java.util.Optional;

public interface GrpcClientUsersService {

    Optional<GetGrpcUserResponse> getUserById(String userId);

    List<GetGrpcUserResponse> getBatchUsersByIds(List<String> userIds);
}
