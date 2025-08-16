package org.tasker.projectservice.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tasker.projectservice.data.entities.ProjectTag;

@Repository
public interface ProjectTagRepository extends JpaRepository<ProjectTag, Long> {

    ProjectTag findByName(String name);
}
