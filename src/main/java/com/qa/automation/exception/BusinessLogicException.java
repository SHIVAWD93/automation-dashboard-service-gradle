package com.qa.automation.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for business logic violations
 */
public class BusinessLogicException extends RuntimeException {
    
    private final HttpStatus statusCode;
    
    public BusinessLogicException(String message) {
        super(message);
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessLogicException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessLogicException(String message, Throwable cause, HttpStatus statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}