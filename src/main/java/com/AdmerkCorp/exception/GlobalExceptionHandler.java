package com.AdmerkCorp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<Object> handleUserNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler({ResourceAlreadyExistsException.class})
    @ResponseStatus(CONFLICT)
    public ResponseEntity<Object> handleUserAlreadyExistsException(ResourceAlreadyExistsException exception) {
        return ResponseEntity
                .status(CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler({AccessForbiddenException.class})
    @ResponseStatus(FORBIDDEN)
    public ResponseEntity<Object> handleForbiddenException(AccessForbiddenException exception) {
        return ResponseEntity
                .status(FORBIDDEN)
                .body(exception.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException exception) {
        return new ErrorResponse(
                FORBIDDEN.value(),
                FORBIDDEN.getReasonPhrase(),
                exception.getMessage()
        );
    }
}
