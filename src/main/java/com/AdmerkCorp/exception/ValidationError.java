package com.AdmerkCorp.exception;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ValidationError {

    private final List<Object> loc;
    private final String msg;
    private final String type;

    public ValidationError(Object loc, String msg, String type) {
        this.loc = Arrays.asList(loc, 0);
        this.msg = msg;
        this.type = type;
    }

}