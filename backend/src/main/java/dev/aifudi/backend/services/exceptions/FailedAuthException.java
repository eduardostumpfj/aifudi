package dev.aifudi.backend.services.exceptions;

public class FailedAuthException extends RuntimeException {
    public FailedAuthException(String message) {
        super(message);
    }
}
