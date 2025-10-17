package org.tasker.projectservice.services;

import org.springframework.data.domain.Pageable;
import org.tasker.projectservice.api.dto.add.AddProjectMemberResponse;
import org.tasker.projectservice.api.dto.add.AddProjectTagsRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectMemberRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectResponse;
import org.tasker.projectservice.api.dto.get.GetProjectResponse;
import org.tasker.projectservice.api.dto.update.UpdateProjectRequest;
import org.tasker.projectservice.api.dto.update.UpdateProjectResponse;
import org.tasker.projectservice.api.dto.update.UpdateStatusRequest;
import org.tasker.projectservice.api.dto.update.UpdateStatusResponse;

import java.time.LocalDate;
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
    List<GetProjectResponse> getAllProjects(Pageable pageable, LocalDate startDate, LocalDate endDate, String projectName);

    UpdateProjectResponse updateProject(UpdateProjectRequest request, Long projectId);

    UpdateStatusResponse updateStatus(UpdateStatusRequest request, Long projectId);

    void addProjectTags(Long projectId, AddProjectTagsRequest request);

    void deleteProjectTags(Long projectId, String tagName, boolean isFull);
}
