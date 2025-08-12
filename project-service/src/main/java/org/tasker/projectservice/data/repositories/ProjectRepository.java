package org.tasker.projectservice.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tasker.projectservice.data.entities.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
