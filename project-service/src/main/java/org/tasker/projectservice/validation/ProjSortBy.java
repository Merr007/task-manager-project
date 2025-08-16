package org.tasker.projectservice.validation;

public enum ProjSortBy {
    PROJECT_NAME("name"),
    START_DATE("startDate"),
    END_DATE("endDate"),
    STATUS("status");

    private final String sortBy;

    ProjSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public static ProjSortBy fromString(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return null;
        }

        for (ProjSortBy projSortBy : ProjSortBy.values()) {
            if (projSortBy.getSortBy().equalsIgnoreCase(sortBy)) {
                return projSortBy;
            }
        }
        return null;
    }

    public String getSortBy() {
        return sortBy;
    }
}
