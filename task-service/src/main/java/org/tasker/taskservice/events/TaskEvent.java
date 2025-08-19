package org.tasker.taskservice.events;

import lombok.Builder;
import lombok.Value;
import org.tasker.taskservice.domain.TaskPriority;
import org.tasker.taskservice.domain.TaskStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class TaskEvent {
    UUID eventId;
    TaskEventType type;
    UUID taskId;
    OffsetDateTime occurredAt;
    Payload payload;

    @Value
    @Builder
    public static class Payload {
        UUID projectId;
        String title;
        String description;
        TaskStatus status;
        TaskPriority priority;
        UUID assigneeId;
        UUID reporterId;
        OffsetDateTime dueAt;
        long version;
    }
}
