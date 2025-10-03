package org.tasker.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.tasker.common.grpc.exception.GrpcException;

import java.util.List;

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

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public ResponseEntity<ExceptionContainer> handleHttpClientErrorException(HttpClientErrorException e) {
        ExceptionContainer container = new ExceptionContainer(e);
        return ResponseEntity.status(e.getStatusCode()).body(container);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<ExceptionContainer> handleBindException(BindException e) {
        ExceptionContainer container = new ExceptionContainer(e, processValidationErrors(e.getFieldErrors()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(container);
    }

    @ExceptionHandler(GrpcException.class)
    @ResponseBody
    public ResponseEntity<ExceptionContainer> handleGrpcException(GrpcException e) {
        ExceptionContainer container = new ExceptionContainer(e);
        return ResponseEntity.status(e.getStatus()).body(container);
    }

    @ExceptionHandler(NotFoundTypeException.class)
    @ResponseBody
    public ResponseEntity<String> handleNotFoundTypeException(NotFoundTypeException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    private String processValidationErrors(List<FieldError> errors) {
        StringBuilder sb = new StringBuilder();

        sb.append("Validation Errors: ");
        for (int i = 0; i < errors.size(); i++) {
            sb.append(errors.get(i).getField()).append(" - ").append(errors.get(i).getDefaultMessage());
            if (i != errors.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

}
