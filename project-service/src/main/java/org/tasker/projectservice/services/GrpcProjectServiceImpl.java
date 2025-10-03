package org.tasker.projectservice.services;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.tasker.common.grpc.*;
import org.tasker.projectservice.exception.NoSuchProjectException;

import java.util.List;

/**
 * gRPC сервис имплементация для project-service
 */
@GrpcService
@Slf4j
@RequiredArgsConstructor
public class GrpcProjectServiceImpl extends ProjectServiceGrpc.ProjectServiceImplBase {

    private final ProjectMembersService projectMembersService;
    private final ProjectService projectService;

    @Override
    public void getProjectMembersByProjectId(GetProjectMembersRequest request,
                                             StreamObserver<GetProjectMembersResponse> responseObserver) {
        try {
            log.info("gRPC call: getProjectMembersByProjectId for projectId: {}", request.getProjectId());

            List<String> memberIds = projectMembersService.getAllProjectMemberIds(request.getProjectId());

            GetProjectMembersResponse response = GetProjectMembersResponse.newBuilder()
                    .setId(request.getProjectId())
                    .addAllMembers(memberIds)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

            log.info("Successfully returned {} members for project {}", memberIds.size(), request.getProjectId());

        } catch (NoSuchProjectException e) {
            log.warn("Project not found: {}", request.getProjectId());
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Project with id " + request.getProjectId() + " not found")
                    .asRuntimeException());
        } catch (Exception e) {
            log.error("Error in getProjectMembersByProjectId", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .asRuntimeException());
        }
    }

    @Override
    public void projectExistsById(ProjectExistsByIdRequest request, StreamObserver<ProjectExistsByIdResponse> responseObserver) {
        boolean projectExists;
        try {
            log.info("gRPC call: projectExistsById for projectId: {}", request.getProjectId());
            projectService.getProject(request.getProjectId());
            projectExists = true;
        } catch (NoSuchProjectException e) {
            projectExists = false;
        }

        ProjectExistsByIdResponse response = ProjectExistsByIdResponse.newBuilder()
                .setExists(projectExists)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
        log.info("Successfully returned {} existing project {}", projectExists, request.getProjectId());
    }
}
