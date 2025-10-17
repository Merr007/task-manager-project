package org.tasker.taskservice.validation;

public enum TaskSortBy {
    TASK_TITLE("title"),
    DUE_DATE("dueDate"),
    PRIORITY("priority"),
    STATUS("status");

    private final String sortBy;

    TaskSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public static TaskSortBy fromString(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return null;
        }

        for (TaskSortBy projSortBy : TaskSortBy.values()) {
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
