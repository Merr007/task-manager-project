package org.tasker.projectservice.services;

import org.springframework.data.domain.Pageable;
import org.tasker.projectservice.api.dto.create.CreateProjectRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectResponse;
import org.tasker.projectservice.api.dto.get.GetProjectResponse;

import java.util.List;

public interface ProjectService {

    /**
     * Creates project based on {@link CreateProjectRequest}
     * @param request Create request
     * @return Response with created project
     */
    CreateProjectResponse createProject(CreateProjectRequest request);

    /**
     * Gets project by specific ID
     * @param projectId ID of project
     * @return Response with project info
     */
    GetProjectResponse getProject(Long projectId);

    /**
     * Deletes project with specific ID
     * @param projectId ID of project
     */
    void deleteProject(Long projectId);

    /**
     * Gets all project
     * @param pageable Pagination info for getting all projects
     * @return A list of {@link GetProjectResponse}
     */
    List<GetProjectResponse> getAllProjects(Pageable pageable);
}
