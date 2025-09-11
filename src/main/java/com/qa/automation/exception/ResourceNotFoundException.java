package com.qa.automation.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {
    
    private final HttpStatus statusCode;
    
    public ResourceNotFoundException(String message) {
        super(message);
        this.statusCode = HttpStatus.NOT_FOUND;
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = HttpStatus.NOT_FOUND;
    }
    
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
