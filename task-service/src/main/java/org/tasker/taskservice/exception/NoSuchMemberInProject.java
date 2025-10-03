package org.tasker.taskservice.exception;

import org.tasker.common.exception.NotFoundTypeException;

public class NoSuchMemberInProject extends NotFoundTypeException {
    public NoSuchMemberInProject(String message) {
        super(message);
    }
}
