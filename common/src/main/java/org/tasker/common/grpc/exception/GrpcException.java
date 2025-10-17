package org.tasker.common.grpc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GrpcException extends RuntimeException {
    private final HttpStatus status;

    public GrpcException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
