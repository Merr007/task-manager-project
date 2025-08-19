package org.tasker.taskservice.api.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.tasker.taskservice.domain.TaskPriority;

import java.time.OffsetDateTime;

@Data
public class TaskUpdateDto {
    @Size(max = 255)
    private String title;
    @Size(max = 20_000)
    private String description;
    private TaskPriority priority;
    private OffsetDateTime dueAt;
}
