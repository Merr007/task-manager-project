package org.tasker.taskservice.data.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tasker.taskservice.data.entities.TaskComment;

import java.util.List;
import java.util.Optional;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {

    Page<TaskComment> findByTaskId(Long taskId, Pageable pageable);

    List<TaskComment> findByUserId(String userId);

    Optional<TaskComment> findByIdAndTaskIdAndUserId(Long id, Long taskId, String userId);
}
