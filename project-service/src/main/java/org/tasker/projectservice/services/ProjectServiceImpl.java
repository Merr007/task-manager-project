package org.tasker.projectservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasker.projectservice.api.dto.create.CreateProjectRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectResponse;
import org.tasker.projectservice.api.dto.get.GetProjectResponse;
import org.tasker.projectservice.data.entities.Project;
import org.tasker.projectservice.data.entities.ProjectMember;
import org.tasker.projectservice.data.repositories.ProjectRepository;
import org.tasker.projectservice.exception.NoSuchProjectException;
import org.tasker.projectservice.mappers.ProjectMapper;

import java.util.List;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;
    private ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Transactional
    public CreateProjectResponse createProject(CreateProjectRequest request) {
        Project project = projectMapper.toProject(request);

        if (request.projectMembers() != null) {
            List<ProjectMember> members = request.projectMembers().stream()
                    .map(projectMapper::toProjectMember)
                    .peek(projectMember -> projectMember.setProject(project))
                    .toList();
            project.setProjectMembers(members);
        }

        Project savedProject = projectRepository.save(project);

        log.info("Created project {}", savedProject);
        return new CreateProjectResponse(savedProject.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public GetProjectResponse getProject(Long projectId) throws NoSuchProjectException {
        Project project = projectRepository.getProjectById(projectId)
                .orElseThrow(() -> new NoSuchProjectException("No such project with id " + projectId));
        log.info("Trying to get project with id {}", projectId);

        return projectMapper.toGetProjectResponse(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        projectRepository.getProjectById(projectId)
                .orElseThrow(() -> new NoSuchProjectException("No such project with id " + projectId));
        projectRepository.deleteProjectById(projectId);

        log.info("Deleted project with id {}", projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetProjectResponse> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable).stream()
                .map(projectMapper::toGetProjectResponse)
                .toList();
    }


}
