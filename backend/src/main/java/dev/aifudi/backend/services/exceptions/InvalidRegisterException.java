package dev.aifudi.backend.services.exceptions;

public class InvalidRegisterException extends RuntimeException {
    public final String field;
    public InvalidRegisterException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField(){
        return this.field;
    }
}
