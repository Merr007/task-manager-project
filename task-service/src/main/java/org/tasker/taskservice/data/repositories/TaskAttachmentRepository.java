package org.tasker.taskservice.data.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tasker.taskservice.data.entities.TaskAttachment;

import java.util.List;

@Repository
public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {

    List<TaskAttachment> findByTaskId(Long taskId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM task_attachments ta WHERE ta.id = :attachmentId AND ta.task.id = :taskId")
    int deleteByTaskIdAndId(@Param("taskId") Long taskId, @Param("attachmentId") Long attachmentId);

}
