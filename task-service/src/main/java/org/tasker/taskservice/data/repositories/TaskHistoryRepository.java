package org.tasker.taskservice.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tasker.taskservice.data.entities.TaskHistory;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
}
