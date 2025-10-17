package org.tasker.taskservice.data.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.tasker.taskservice.data.entities.TaskComment;

import java.util.ArrayList;
import java.util.List;

public class TaskCommentSpecification {

    private TaskCommentSpecification(){}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Specification<TaskComment>> specifications = new ArrayList<>();

        public Builder withTaskId(final Long taskId) {
            specifications.add(taskIdEqual(taskId));
            return this;
        }

        public Builder withUserId(final String userId) {
            specifications.add(userIdEqual(userId));
            return this;
        }

        public Specification<TaskComment> build() {
            Specification<TaskComment> combined = Specification.unrestricted();
            for (Specification<TaskComment> specification : specifications) {
                combined = combined.and(specification);
            }
            return combined;
        }

        private Specification<TaskComment> taskIdEqual(final Long taskId) {
            return ((root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                if (taskId != null) {
                    predicate = criteriaBuilder.equal(root.get("task").get("id"), taskId);
                }
                return predicate;
            });
        }

        private Specification<TaskComment> userIdEqual(final String userId) {
            return ((root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                if (userId != null) {
                    predicate = criteriaBuilder.equal(root.get("userId"), userId);
                }
                return predicate;
            });
        }
    }
}
