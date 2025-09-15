package org.tasker.projectservice.data.repositories;

import org.tasker.projectservice.data.entities.ProjectMember;

import java.util.List;

public interface ProjectMemberRepository {
    List<ProjectMember> findByProjectId(Long projectId, int limit, int offset);
}
