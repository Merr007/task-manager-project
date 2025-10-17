package org.tasker.usermanagementservice.exception;

public class RefreshTokenExpirationException extends RuntimeException {
    public RefreshTokenExpirationException(String message) {
        super(message);
    }
}
