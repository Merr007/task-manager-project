package org.tasker.common.exception;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.tasker.common.source.ExceptionMessageSource;

import java.util.Locale;

@Getter
public class RestException extends RuntimeException {
    private final HttpStatus httpStatus;

    private final MessageSource messageSource = new ExceptionMessageSource();

    public RestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    @Override
    public String getLocalizedMessage() {
        return messageSource.getMessage(super.getMessage(), null, Locale.getDefault());
    }
}
