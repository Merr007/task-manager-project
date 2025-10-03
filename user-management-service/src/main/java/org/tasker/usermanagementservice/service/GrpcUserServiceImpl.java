package org.tasker.usermanagementservice.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.tasker.common.api.dto.user.GetUserResponse;
import org.tasker.common.grpc.*;
import org.tasker.usermanagementservice.exception.UserNotFoundException;
import org.tasker.usermanagementservice.mappers.UsersMapper;

import java.util.List;

@GrpcService
@Slf4j
@RequiredArgsConstructor
public class GrpcUserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    private final UsersMapper usersMapper;

    @Override
    public void getUserById(GetGrpcUserRequest request,
                            StreamObserver<GetGrpcUserResponse> responseObserver) {
        try {
            log.info("gRPC call: getUserById for user id {}", request.getUserId());

            GetUserResponse userById = userService.getUserById(request.getUserId());

            GetGrpcUserResponse response = GetGrpcUserResponse.newBuilder()
                    .setUserId(userById.id())
                    .setUsername(userById.username())
                    .setFirstName(userById.firstName())
                    .setLastName(userById.lastName())
                    .setEmail(userById.email())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (UserNotFoundException e) {
            log.warn("User not found: {}", request.getUserId());
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("User with id " + request.getUserId() + " not found")
                    .asRuntimeException());
        } catch (Exception e) {
            log.warn("Unexpected error in getUserById", e);
            responseObserver.onError(Status.INTERNAL.asRuntimeException());
        }
    }

    @Override
    public void getBatchUsersById(GetBatchUsersRequest request,
                                  StreamObserver<GetBatchUsersResponse> responseObserver) {
        try {
            log.info("gRPC call: getUserById for batch users");
            List<GetUserResponse> users = userService.getBatchUsers(usersMapper.toGetUserRequest(request.getUserIdsList()));

            GetBatchUsersResponse response = GetBatchUsersResponse.newBuilder()
                    .addAllUsers(usersMapper.toGetGrpcUsersResponse(users))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.warn("Unexpected error in getBatchUsersById", e);
            responseObserver.onError(Status.INTERNAL.asRuntimeException());
        }

    }
}
