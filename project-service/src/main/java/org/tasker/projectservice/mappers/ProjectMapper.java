package org.tasker.projectservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.tasker.projectservice.api.dto.create.CreateProjectMemberRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectTagRequest;
import org.tasker.projectservice.api.dto.get.GetProjectResponse;
import org.tasker.projectservice.api.dto.get.GetProjectTag;
import org.tasker.projectservice.data.entities.Project;
import org.tasker.projectservice.data.entities.ProjectMember;
import org.tasker.projectservice.data.entities.ProjectTag;
import org.tasker.projectservice.data.repositories.ProjectTagRepository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class ProjectMapper {

    @Autowired
    private ProjectTagRepository projectTagRepository;

    @Mapping(target = "name", source = "projectName")
    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "projectMembers", ignore = true)
    @Mapping(target = "tags", source = "projectTags")
    @Mapping(target = "attachments", ignore = true)
    @Mapping(target = "teamId", ignore = true)
    public abstract Project toProject(CreateProjectRequest createProjectRequest);

    @Mapping(target = "role", source = "role", defaultExpression = "java(org.tasker.projectservice.domain.model.ProjectRole.VIEWER)")
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "joinedAt", expression = "java(java.time.LocalDateTime.now())")
    public abstract ProjectMember toProjectMember(CreateProjectMemberRequest projectMemberRequest);

    @Mapping(target = "projects", ignore = true)
    public abstract ProjectTag toProjectTag(CreateProjectTagRequest projectTagRequest);

    public abstract GetProjectResponse toGetProjectResponse(Project project);

    public abstract GetProjectTag toGetProjectTag(ProjectTag projectTag);

    public Set<ProjectTag> mapProjectTags(Set<CreateProjectTagRequest> projectTagsRequest) {
        if (projectTagsRequest == null) {
            return null;
        }
        Set<ProjectTag> projectTags = new HashSet<>();
        for (CreateProjectTagRequest projectTagRequest : projectTagsRequest) {
            ProjectTag projectTag = toProjectTag(projectTagRequest);
            ProjectTag existingTag = projectTagRepository.findByName(projectTag.getName());
            projectTags.add(Objects.requireNonNullElseGet(existingTag, () -> projectTagRepository.save(projectTag)));
        }
        return projectTags;
    }
}
