package org.tasker.taskservice.services;

import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tasker.common.grpc.*;
import org.tasker.common.grpc.exception.GrpcExceptionHandler;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrpcClientProjectServiceImpl implements GrpcClientProjectService {

    private final ProjectServiceGrpc.ProjectServiceBlockingStub projectServiceStub;

    @Override
    public List<String> getProjectMembers(Long projectId) {
        try {
            GetProjectMembersRequest request = GetProjectMembersRequest.newBuilder()
                    .setProjectId(projectId)
                    .build();

            log.info("Calling gRPC project-service for project members, projectId: {}", projectId);
            GetProjectMembersResponse response = projectServiceStub.getProjectMembersByProjectId(request);
            log.info("Received response from project-service: {} members", response.getMembersCount());

            return response.getMembersList();
        } catch (StatusRuntimeException e) {
            GrpcExceptionHandler.handleGrpcError(e);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean existsByProjectId(Long projectId) {
        try {
            ProjectExistsByIdRequest request = ProjectExistsByIdRequest.newBuilder()
                    .setProjectId(projectId)
                    .build();

            log.info("Calling gRPC project-service for project exists, projectId: {}", projectId);
            ProjectExistsByIdResponse response = projectServiceStub.projectExistsById(request);
            log.info("Received response from project-service: {} exists", response.getExists());
            return response.getExists();
        } catch (StatusRuntimeException e) {
            GrpcExceptionHandler.handleGrpcError(e);
        }
        return false;
    }

}
