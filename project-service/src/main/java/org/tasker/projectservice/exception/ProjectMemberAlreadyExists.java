package org.tasker.projectservice.exception;

public class ProjectMemberAlreadyExists extends RuntimeException {
    public ProjectMemberAlreadyExists(String message) {
        super(message);
    }
}
