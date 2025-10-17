package org.tasker.projectservice.api.dto.update;

import jakarta.validation.constraints.NotNull;
import org.tasker.projectservice.domain.model.ProjectStatus;

public record UpdateStatusRequest(
        @NotNull
        ProjectStatus status
) {
}
