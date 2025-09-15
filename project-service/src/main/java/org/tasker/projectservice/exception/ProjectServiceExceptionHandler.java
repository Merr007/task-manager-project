package org.tasker.projectservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.tasker.common.exception.AbstractExceptionHandler;

@RestControllerAdvice
public class ProjectServiceExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(S3ObjectNotFoundException.class)
    public ResponseEntity<String> handleS3ObjectNotFoundException(S3ObjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
