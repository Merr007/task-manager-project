package org.tasker.common.grpc.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class GrpcExceptionHandler {

    private GrpcExceptionHandler() {
    }

    public static void handleGrpcError(StatusRuntimeException e) {
        Status status = e.getStatus();
        String description = status.getDescription();

        HttpStatus httpStatus;
        log.warn("gRPC error: {} - {}", status.getCode(), description);

        switch (status.getCode()) {
            case NOT_FOUND -> {
                log.warn("Not found: {}", description);
                httpStatus = HttpStatus.NOT_FOUND;
            }

            case INVALID_ARGUMENT -> {
                log.warn("Invalid argument: {}", description);
                httpStatus = HttpStatus.BAD_REQUEST;
            }

            case PERMISSION_DENIED -> {
                log.error("Access denied: {}", description);
                httpStatus = HttpStatus.FORBIDDEN;
            }

            case UNAUTHENTICATED -> {
                log.error("Authentication failed: {}", description);
                httpStatus = HttpStatus.UNAUTHORIZED;
            }

            case UNAVAILABLE -> {
                log.error("gRPC service unavailable: {}", description);
                httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            }

            default -> {
                log.error("Unexpected gRPC error: {} - {}", status.getCode(), description);
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        throw assembleException(httpStatus, description);
    }

    private static GrpcException assembleException(HttpStatus status, String description) {
        return new GrpcException(status, description);
    }
}
