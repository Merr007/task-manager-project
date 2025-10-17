package org.tasker.taskservice.data.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tasker.taskservice.data.entities.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from tasks t where t.id = :task_id and t.projectId = :project_id")
    Optional<Task> findByTaskIdAndProjectId(@Param("task_id") Long taskId, @Param("project_id") Long projectId);

    @Query("select t from tasks t left join fetch t.attachments where t.id = :task_id")
    Optional<Task> getTaskWithAttachments(@Param("task_id") Long taskId);

    List<Task> findAll(Specification<Task> spec, Pageable pageable);
}
