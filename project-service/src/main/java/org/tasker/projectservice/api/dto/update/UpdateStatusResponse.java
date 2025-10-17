package org.tasker.projectservice.api.dto.update;

import org.tasker.projectservice.domain.model.ProjectStatus;

public record UpdateStatusResponse(
        ProjectStatus projectStatus
) {
}
