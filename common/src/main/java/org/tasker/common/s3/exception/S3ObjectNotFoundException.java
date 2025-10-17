package org.tasker.common.s3.exception;

public class S3ObjectNotFoundException extends RuntimeException {
    public S3ObjectNotFoundException(String message) {
        super(message);
    }
}
