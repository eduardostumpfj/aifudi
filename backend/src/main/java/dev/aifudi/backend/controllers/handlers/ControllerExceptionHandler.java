package dev.aifudi.backend.controllers.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import dev.aifudi.backend.dtos.erros.ErrorDTO;
import dev.aifudi.backend.dtos.erros.ValidationErrorDTO;
import dev.aifudi.backend.services.exceptions.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            InvalidParamException.class,
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
    public ResponseEntity<ErrorDTO> handleDuplicatedEmail(DuplicateKeyException error){
        String cause = error.getMostSpecificCause().getMessage();
        String message;

        // Check witch field is duplicated
        if(cause.contains("users_login_key")){
            message = "An account with this login already exists";
        } else if (cause.contains("users_email_key")) {
            message = "An account with this email already exists";
        } else {
            message = "An account with this information already exists";
        }

        var status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status.value()).body(new ErrorDTO(message, status.value()));
    }

    @ExceptionHandler(FailedAuthException.class)
    public ResponseEntity<ErrorDTO> handleFailedAuthException (FailedAuthException error){
        var status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status.value()).body(new ErrorDTO(error.getMessage(), status.value()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleInvalidRoleException (HttpMessageNotReadableException error){
        var status = HttpStatus.BAD_REQUEST;
        String fullMessage = error.getMostSpecificCause().getMessage();
        if(fullMessage.contains("roleName")){
            Throwable cause = error.getCause();
            if(cause instanceof InvalidFormatException invalid){
               Object value = invalid.getValue();
               String errorMessage = "roleName: " + value + " is not an option";
               return ResponseEntity.status(status.value()).body(new ErrorDTO(errorMessage, status.value()));
            }
        }

        return ResponseEntity.status(status.value()).build();
    }
}
