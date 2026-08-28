package dev.aifudi.backend.controllers.handlers;

import dev.aifudi.backend.services.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException (ResourceNotFoundException error, HttpServletRequest request) {
        return createProblem(
                HttpStatus.BAD_REQUEST,
                "Resource Not Found",
                error.getMessage(),
                request
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException (NotFoundException error, HttpServletRequest request){
        return createProblem(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                error.getMessage(),
                request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException (AccessDeniedException error, HttpServletRequest request) {
        return createProblem(
                HttpStatus.FORBIDDEN,
                "Not Allowed",
                error.getMessage(),
                request
        );
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            InvalidRegisterException.class,
            MissingServletRequestParameterException.class,
            InvalidParamException.class,
    })
    public ProblemDetail handleMethodValidationException (Exception error,  HttpServletRequest request) {
        var problem = createProblem(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more fields are invalid",
                request
        );

        List<String> errors = new ArrayList<>();

        // Invalid fields
        if (error instanceof MethodArgumentNotValidException ex) {
            for (var e : ex.getBindingResult().getFieldErrors()) {
                errors.add(e.getField() + ": " + e.getDefaultMessage());
            }
        }

        // Invalid role
        if (error instanceof InvalidRegisterException ex) {
            errors.add(ex.getField() + ": " + ex.getMessage());
        }

        // Invalid params
        if (error instanceof MissingServletRequestParameterException ex){
            errors.add(ex.getParameterName() + ": " + ex.getMessage());
        }

        // Empty param value
        if (error instanceof InvalidParamException ex) {
            errors.add(ex.getField() + ": " + ex.getMessage());
        }

        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ProblemDetail handleDuplicated(DuplicateKeyException error, HttpServletRequest request) {
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

        return createProblem(
                HttpStatus.CONFLICT,
                "Duplicate resource",
                message,
                request
        );
    }

    @ExceptionHandler(FailedAuthException.class)
    public ProblemDetail handleFailedAuthException (FailedAuthException error,  HttpServletRequest request) {
        return createProblem(
                HttpStatus.UNAUTHORIZED,
                "Authentication failed",
                error.getMessage(),
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidRoleException (HttpMessageNotReadableException error,HttpServletRequest request){
        var status = HttpStatus.BAD_REQUEST;
        String fullMessage = error.getMostSpecificCause().getMessage();

        if(fullMessage.contains("roleName")){
            Throwable cause = error.getCause();
            if(cause instanceof InvalidFormatException invalid){
               Object value = invalid.getValue();

               return createProblem(
                        status,
                        "Invalid request body",
                        "roleName: " + value + " is not an option",
                        request
               );
            }
        }

        return createProblem(
                status,
                "Invalid request body",
                "The request body is invalid",
                request
        );
    }


    private ProblemDetail createProblem(
            HttpStatus status,
            String title,
            String detail,
            HttpServletRequest request
    ){
        var problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setInstance(java.net.URI.create(request.getRequestURI()));

        return problem;
    }
}
