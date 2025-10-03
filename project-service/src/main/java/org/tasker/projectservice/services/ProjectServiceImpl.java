package org.tasker.projectservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import org.tasker.projectservice.data.entities.Project;
import org.tasker.projectservice.data.entities.ProjectMember;
import org.tasker.projectservice.data.entities.ProjectTag;
import org.tasker.projectservice.data.repositories.ProjectRepository;
import org.tasker.projectservice.data.repositories.ProjectTagRepository;
import org.tasker.projectservice.data.specification.ProjectSpecification;
import org.tasker.projectservice.exception.NoSuchMemberException;
import org.tasker.projectservice.exception.NoSuchProjectException;
import org.tasker.projectservice.exception.NoSuchTagException;
import org.tasker.projectservice.exception.ProjectMemberAlreadyExists;
import org.tasker.projectservice.mappers.ProjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectTagRepository projectTagRepository;

    @Transactional
    @Override
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
                .orElseThrow(() -> new NoSuchProjectException("No such project with memberId " + projectId));
        log.info("Trying to get project with memberId {}", projectId);

        return projectMapper.toGetProjectResponse(project);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        projectRepository.getProjectById(projectId)
                .orElseThrow(() -> new NoSuchProjectException("No such project with memberId " + projectId));
        projectRepository.deleteProjectById(projectId);

        log.info("Deleted project with memberId {}", projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetProjectResponse> getAllProjects(Pageable pageable, LocalDate startDate, LocalDate endDate, String projectName) {
        Specification<Project> spec = ProjectSpecification.builder()
                .withBetweenDates(startDate, endDate)
                .withProjectNameLike(projectName)
                .build();
        return projectRepository.findAll(spec, pageable).stream()
                .map(projectMapper::toGetProjectResponse)
                .toList();
    }

    @Override
    @Transactional
    public UpdateProjectResponse updateProject(UpdateProjectRequest request, Long projectId) {
        Project project = projectRepository.getProjectById(projectId).orElseThrow(() -> new NoSuchProjectException("No such project"));

        project.setName(request.projectName());

        if (request.description() != null) {
            project.setDescription(request.description());
        }

        if (request.startDate() != null) {
            project.setStartDate(request.startDate());
        }

        if (request.endDate() != null) {
            project.setEndDate(request.endDate());
        }

        return projectMapper.toUpdateProjectResponse(project);
    }

    @Override
    @Transactional
    public UpdateStatusResponse updateStatus(UpdateStatusRequest request, Long projectId) {
        Project project = projectRepository.getProjectById(projectId).orElseThrow(() -> new NoSuchProjectException("No such project"));

        project.setStatus(request.status());

        return new UpdateStatusResponse(project.getStatus());
    }

    @Override
    @Transactional
    public void addProjectTags(Long projectId, AddProjectTagsRequest request) {
        Project project = projectRepository.getProjectById(projectId).orElseThrow(() -> new NoSuchProjectException("No such project"));
        Set<String> projectTagsNames = project.getTags().stream()
                .map(ProjectTag::getName)
                .collect(Collectors.toSet());

        Set<ProjectTag> filteredTags = request.tags().stream()
                .filter(tag -> !projectTagsNames.contains(tag.name()))
                .map(tag -> {
                    ProjectTag existingTag = projectTagRepository.findByName(tag.name());
                    ProjectTag addTag = existingTag == null ? projectMapper.toNewProjectTag(tag.name(), project) : existingTag;
                    if (addTag.getProjects().isEmpty()) {
                        addTag.setProjects(Set.of(project));
                    }
                    return addTag;
                })
                .collect(Collectors.toSet());
        project.getTags().addAll(filteredTags);
    }

    @Override
    @Transactional
    public void deleteProjectTags(Long projectId, String tagName, boolean isFull) {
        Project project = projectRepository.getProjectById(projectId).orElseThrow(() -> new NoSuchProjectException("No such project"));

        if (!projectTagRepository.existsByName(tagName)) {
            throw new NoSuchTagException("No such tag: " + tagName);
        }

        ProjectTag projectTag = project.getTags().stream()
                .filter(tag -> tag.getName().equals(tagName))
                .findFirst()
                .orElseThrow(() -> new NoSuchTagException("No such tag: " + tagName + " in project: " + projectId));

        project.getTags().remove(projectTag);

        if (isFull) {
            projectTagRepository.deleteByName(tagName);
        }
    }

}
