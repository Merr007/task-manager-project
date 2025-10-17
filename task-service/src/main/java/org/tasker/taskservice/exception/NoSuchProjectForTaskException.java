package org.tasker.taskservice.exception;

import org.tasker.common.exception.NotFoundTypeException;

public class NoSuchProjectForTaskException extends NotFoundTypeException {
    public NoSuchProjectForTaskException(String message) {
        super(message);
    }
}
