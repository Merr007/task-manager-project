package org.tasker.taskservice.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tasker.taskservice.data.entities.Task;
import org.tasker.taskservice.domain.TaskStatus;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findByProjectId(UUID projectId, Pageable pageable);
    Page<Task> findByAssigneeId(UUID assigneeId, Pageable pageable);
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);
}
