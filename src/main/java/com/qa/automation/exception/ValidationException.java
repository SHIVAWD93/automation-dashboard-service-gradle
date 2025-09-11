package com.qa.automation.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for validation errors
 */
public class ValidationException extends RuntimeException {
    
    private final HttpStatus statusCode;
    
    public ValidationException(String message) {
        super(message);
        this.statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
    }
    
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}