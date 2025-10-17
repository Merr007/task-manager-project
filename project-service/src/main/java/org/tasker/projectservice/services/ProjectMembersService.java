package org.tasker.projectservice.services;

import org.tasker.projectservice.api.dto.add.AddProjectMemberResponse;
import org.tasker.projectservice.api.dto.create.CreateProjectMemberRequest;
import org.tasker.projectservice.api.dto.get.GetMemberResponse;

import java.util.List;

public interface ProjectMembersService {
    AddProjectMemberResponse addProjectMember(CreateProjectMemberRequest request, Long projectId);

    void removeProjectMember(Long projectId, Long memberId);

    List<GetMemberResponse> getAllProjectMembers(Long projectId, int limit, int offset);

    List<String> getAllProjectMemberIds(Long projectId);

}
