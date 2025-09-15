package org.tasker.projectservice.data.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.tasker.projectservice.data.entities.Project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectSpecification {

    public static Builder builder() {
        return new Builder();
    }

    private ProjectSpecification() {}

    public static class Builder {

        private final List<Specification<Project>> specifications = new ArrayList<>();

        public Builder withBetweenDates(LocalDate startDate, LocalDate endDate) {
            specifications.add(betweenDates(startDate, endDate));
            return this;
        }

        public Builder withProjectNameLike(String projectName) {
            specifications.add(projectNameLike(projectName));
            return this;
        }

        public Specification<Project> build() {
            Specification<Project> combined = Specification.unrestricted();
            for (Specification<Project> specification : specifications) {
                combined = combined.and(specification);
            }
            return combined;
        }

        private Specification<Project> betweenDates(LocalDate startDate, LocalDate endDate) {
            return ((root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                if (startDate != null) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
                }
                if (endDate != null) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), endDate));
                }

                if (startDate != null) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), startDate));
                }
                if (endDate != null) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
                }
                return predicate;
            });
        }

        private Specification<Project> projectNameLike(String projectName) {
            return ((root, query, criteriaBuilder) -> {
                Predicate predicate = criteriaBuilder.conjunction();
                if (projectName != null) {
                    predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), ("%" + projectName + "%").toLowerCase());
                }
                return predicate;
            });
        }
    }
}
