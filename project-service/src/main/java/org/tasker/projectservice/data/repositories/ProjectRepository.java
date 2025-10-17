package org.tasker.projectservice.data.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tasker.projectservice.data.entities.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"tags", "projectMembers"})
    Optional<Project> getProjectById(Long id);

    void deleteProjectById(Long id);

    @EntityGraph(attributePaths = {"tags"})
    List<Project> findAll(Specification<Project> spec, Pageable pageable);

    @Query("select p from projects p where p.id = :id")
    @EntityGraph(attributePaths = {"attachments"})
    Optional<Project> getProjectWithAttachments(@Param("id") Long id);
}
