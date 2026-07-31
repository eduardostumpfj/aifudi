package dev.aifudi.backend.controllers.handlers;

import dev.aifudi.backend.dtos.erros.ErrorDTO;
import dev.aifudi.backend.dtos.erros.ValidationErrorDTO;
import dev.aifudi.backend.services.exceptions.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFoundException (ResourceNotFoundException e){
        var status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status.value()).body(new ErrorDTO(e.getMessage(), status.value()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFoundException (NotFoundException e){
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new ErrorDTO(e.getMessage(), status.value()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDeniedException (AccessDeniedException e){
        var status = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status.value()).body(new ErrorDTO(e.getMessage(), status.value()));
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            InvalidRegisterException.class,
            MissingServletRequestParameterException.class,
            InvalidParamException.class
    })
    public ResponseEntity<ValidationErrorDTO> handleMethodValidationException (Exception e){
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();

        // Invalid fields
        if (e instanceof MethodArgumentNotValidException ex) {
            for (var error : ex.getBindingResult().getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
        }

        // Invalid role
        if (e instanceof InvalidRegisterException ex) {
            errors.add(ex.getField() + ": " + ex.getMessage());
        }

        // Invalid params
        if (e instanceof MissingServletRequestParameterException ex){
            errors.add(ex.getParameterName() + ": " + ex.getMessage());
        }

        // Empty param value
        if (e instanceof InvalidParamException ex) {
            errors.add(ex.getField() + ": " + ex.getMessage());
        }


        return ResponseEntity
                .status(status)
                .body(new ValidationErrorDTO(errors, status.value()));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorDTO> handleDuplicatedEmail(){
        var status = HttpStatus.CONFLICT;
        String message = "An account with this email already exists";
        return ResponseEntity.status(status.value()).body(new ErrorDTO(message, status.value()));
    }

    @ExceptionHandler(FailedAuthException.class)
    public ResponseEntity<ErrorDTO> handleFailedAuthException (FailedAuthException error){
        var status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status.value()).body(new ErrorDTO(error.getMessage(), status.value()));
    }
}
