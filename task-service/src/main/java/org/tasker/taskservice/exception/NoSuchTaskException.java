package org.tasker.taskservice.exception;

import org.tasker.common.exception.NotFoundTypeException;

public class NoSuchTaskException extends NotFoundTypeException {
    public NoSuchTaskException(String message) {
        super(message);
    }
}
