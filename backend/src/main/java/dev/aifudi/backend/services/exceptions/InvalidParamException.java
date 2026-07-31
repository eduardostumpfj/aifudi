package dev.aifudi.backend.services.exceptions;

public class InvalidParamException extends RuntimeException {
    public final String field;
    public InvalidParamException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField(){
        return this.field;
    }
}
