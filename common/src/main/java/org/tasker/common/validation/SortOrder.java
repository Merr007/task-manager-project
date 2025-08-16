package org.tasker.common.validation;

public enum SortOrder {
    ASC, DESC;

    public static SortOrder fromString(String sortOrder) {
        if (sortOrder == null || sortOrder.isEmpty()) {
            return ASC;
        }
        try {
            return SortOrder.valueOf(sortOrder.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ASC;
        }
    }
}
