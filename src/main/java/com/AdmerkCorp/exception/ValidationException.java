package com.AdmerkCorp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationException extends RuntimeException {

    private final List<ValidationError> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = Collections.singletonList(new ValidationError("string", message, "string"));
    }

}
