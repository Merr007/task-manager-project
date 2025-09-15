package org.tasker.projectservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasker.common.api.dto.user.GetUserResponse;
import org.tasker.common.client.UsersRestClient;
import org.tasker.projectservice.api.dto.add.AddProjectMemberResponse;
import org.tasker.projectservice.api.dto.create.CreateProjectMemberRequest;
import org.tasker.projectservice.api.dto.get.GetMemberResponse;
import org.tasker.projectservice.data.entities.Project;
import org.tasker.projectservice.data.entities.ProjectMember;
import org.tasker.projectservice.data.repositories.ProjectMemberRepository;
import org.tasker.projectservice.data.repositories.ProjectRepository;
import org.tasker.projectservice.exception.NoSuchMemberException;
import org.tasker.projectservice.exception.NoSuchProjectException;
import org.tasker.projectservice.exception.ProjectMemberAlreadyExists;
import org.tasker.projectservice.mappers.ProjectMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectMembersServiceImpl implements ProjectMembersService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UsersRestClient usersRestClient;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    @Transactional
    public AddProjectMemberResponse addProjectMember(CreateProjectMemberRequest request, Long projectId) {
        Project project = projectRepository.getProjectById(projectId).orElseThrow(() -> new NoSuchProjectException("No such project"));
        List<ProjectMember> projectMembers = project.getProjectMembers();

        boolean exists = projectMembers.stream()
                .anyMatch(pm -> pm.getUserId().equals(request.userId()));

        if (exists) {
            throw new ProjectMemberAlreadyExists("Current user has already added as project member");
        }
        ProjectMember projectMember = projectMapper.toProjectMember(request);
        projectMembers.add(projectMember);
        projectMember.setProject(project);

        Project updatedProject = projectRepository.save(project);

        log.info("Added project member: {}", projectMember);

        return new AddProjectMemberResponse(updatedProject.getProjectMembers().stream()
                .filter(pm -> pm.getUserId().equals(request.userId()))
                .findFirst()
                .get()
                .getId());
    }

    @Override
    @Transactional
    public void removeProjectMember(Long projectId, Long memberId) {
        Project project = projectRepository.getProjectById(projectId).orElseThrow(() -> new NoSuchProjectException("No such project"));
        List<ProjectMember> projectMembers = project.getProjectMembers();

        Optional<ProjectMember> projectMember = projectMembers.stream()
                .filter(pm -> pm.getId().equals(memberId))
                .findFirst();

        if (projectMember.isEmpty()) {
            throw new NoSuchMemberException("No such member in project");
        }
        log.info("Removed project member: {}", projectMember.get());

        projectMembers.remove(projectMember.get());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetMemberResponse> getAllProjectMembers(Long projectId, int limit, int offset) {
        projectRepository.getProjectById(projectId).orElseThrow(() -> new NoSuchProjectException("No such project"));
        return projectMemberRepository.findByProjectId(projectId, limit, offset)
                .stream()
                .map(projectMember -> {
                    Optional<GetUserResponse> userById = usersRestClient.getUserById(projectMember.getUserId());
                    return userById.map(userResponse -> GetMemberResponse.builder()
                            .userId(projectMember.getUserId())
                            .memberId(projectMember.getId())
                            .email(userResponse.email())
                            .username(userResponse.username())
                            .firstName(userResponse.firstName())
                            .lastname(userResponse.lastName())
                            .role(projectMember.getRole())
                            .build()).orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
