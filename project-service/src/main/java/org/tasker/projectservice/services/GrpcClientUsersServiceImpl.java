package org.tasker.projectservice.services;

import io.grpc.StatusRuntimeException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tasker.common.grpc.*;
import org.tasker.common.grpc.exception.GrpcExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class GrpcClientUsersServiceImpl implements GrpcClientUsersService {

    private final UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @Override
    public Optional<GetGrpcUserResponse> getUserById(String userId) {
        try {
            GetGrpcUserRequest request = GetGrpcUserRequest.newBuilder()
                    .setUserId(userId)
                    .build();

            log.info("Calling gRPC user-management-service for user: {}", userId);
            GetGrpcUserResponse response = userServiceBlockingStub.getUserById(request);
            log.info("Received grpc user-management response");

            return Optional.of(response);
        } catch (StatusRuntimeException e) {
            GrpcExceptionHandler.handleGrpcError(e);
        }
        return Optional.empty();
    }

    @Override
    public List<GetGrpcUserResponse> getBatchUsersByIds(List<String> userIds) {
        try {
            GetBatchUsersRequest request = GetBatchUsersRequest.newBuilder()
                    .addAllUserIds(mapIdsToUsersRequestList(userIds))
                    .build();
            return userServiceBlockingStub.getBatchUsersById(request).getUsersList();
        } catch (StatusRuntimeException e) {
            GrpcExceptionHandler.handleGrpcError(e);
        }
        return Collections.emptyList();
    }

    private List<GetGrpcUserRequest> mapIdsToUsersRequestList(List<String> ids) {
        return ids.stream()
                .map(id -> GetGrpcUserRequest.newBuilder()
                        .setUserId(id)
                        .build())
                .toList();
    }
}
