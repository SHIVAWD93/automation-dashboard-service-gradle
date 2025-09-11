package com.qa.automation.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for service layer errors
 */
public class ServiceException extends RuntimeException {
    
    private final HttpStatus statusCode;
    
    public ServiceException(String message) {
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    
    public ServiceException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public ServiceException(String message, Throwable cause, HttpStatus statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}