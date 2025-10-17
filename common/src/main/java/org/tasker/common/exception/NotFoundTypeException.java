package org.tasker.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class NotFoundTypeException extends RuntimeException {
    private HttpStatus status;

    public NotFoundTypeException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }
}
