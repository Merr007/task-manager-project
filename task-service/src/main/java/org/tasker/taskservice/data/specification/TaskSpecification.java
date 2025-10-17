package org.tasker.taskservice.data.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.tasker.taskservice.data.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Specification<Task>> specifications = new ArrayList<>();

        public Builder withProjectId(final Long projectId) {
            specifications.add(projectIdEqual(projectId));
            return this;
        }

        public Builder withTaskTitleLike(final String taskTitle) {
            specifications.add(taskTitleLike(taskTitle));
            return this;
        }
        public Specification<Task> build() {
            Specification<Task> combined = Specification.unrestricted();
            for (Specification<Task> specification : specifications) {
                combined = combined.and(specification);
            }
            return combined;
        }

        private Specification<Task> projectIdEqual(final Long projectId) {
            return ((root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                if (projectId != null) {
                    predicate = criteriaBuilder.equal(root.get("projectId"), projectId);
                }
                return predicate;
            });
        }

        private Specification<Task> taskTitleLike(final String taskTitle) {
            return ((root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                if (taskTitle != null) {
                    predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), ("%" + taskTitle + "%").toLowerCase());
                }
                return predicate;
            });
        }
    }
}
