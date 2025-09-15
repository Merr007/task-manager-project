package org.tasker.projectservice.api.dto.get;

import lombok.Builder;
import org.tasker.projectservice.domain.model.ProjectRole;

@Builder
public record GetMemberResponse(
        Long memberId,
        String userId,
        String username,
        String email,
        String firstName,
        String lastname,
        ProjectRole role
) {
}
