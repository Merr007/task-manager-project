package org.tasker.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
public abstract class AbstractExceptionHandler {

    @ExceptionHandler(RestException.class)
    @ResponseBody
    public ResponseEntity<ExceptionContainer> handleRestException(RestException exception) {
        ExceptionContainer container = new ExceptionContainer(exception);
        return ResponseEntity.status(exception.getHttpStatus()).body(container);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ExceptionContainer> handleException(Exception exception) {
        ExceptionContainer container = new ExceptionContainer(exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(container);
    }
}
