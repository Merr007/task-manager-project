package org.tasker.projectservice.data.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tasker.projectservice.data.entities.ProjectAttachment;

import java.util.List;

@Repository
public interface ProjectAttachmentRepository extends JpaRepository<ProjectAttachment, Long> {

    List<ProjectAttachment> findByProjectId(Long projectId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM project_attachments pa WHERE pa.id = :attachmentId AND pa.project.id = :projectId")
    int deleteByProjectIdAndId(@Param("projectId") Long projectId, @Param("attachmentId") Long attachmentId);

}
