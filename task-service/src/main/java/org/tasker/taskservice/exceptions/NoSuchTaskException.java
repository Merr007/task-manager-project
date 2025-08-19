package org.tasker.taskservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public class NoSuchTaskException extends ResponseStatusException {
    public NoSuchTaskException(String reason) { super(HttpStatus.NOT_FOUND, reason); }
    public static NoSuchTaskException task(UUID id) { return new NoSuchTaskException("Task %s not found".formatted(id)); }
}
