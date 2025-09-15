package org.tasker.projectservice.data.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tasker.projectservice.data.entities.ProjectAttachment;

import java.util.List;

@Repository
public interface ProjectAttachmentRepository extends JpaRepository<ProjectAttachment, Long> {

    List<ProjectAttachment> findByProjectId(Long projectId, Pageable pageable);

}
