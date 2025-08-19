package org.tasker.taskservice.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tasker.taskservice.domain.TaskStatus;

@Data
public class StatusChangeRequest {
    @NotNull
    private TaskStatus status;
}
