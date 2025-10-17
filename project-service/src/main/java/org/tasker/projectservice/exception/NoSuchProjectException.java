package org.tasker.projectservice.exception;

public class NoSuchProjectException extends RuntimeException {
    public NoSuchProjectException(String message) {
        super(message);
    }
}
