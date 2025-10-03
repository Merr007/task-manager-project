package org.tasker.taskservice.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tasker.taskservice.data.entities.TaskAttachment;

public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {
}
