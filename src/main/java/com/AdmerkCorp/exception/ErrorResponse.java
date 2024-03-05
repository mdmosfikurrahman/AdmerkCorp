package com.AdmerkCorp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private int code;
    private String error;
    private String message;

}