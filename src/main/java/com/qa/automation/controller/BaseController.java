package com.qa.automation.controller;

import com.qa.automation.dto.ApiResponse;
import com.qa.automation.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Base controller providing common functionality for all controllers
 * Exception handling is now managed by the global exception handler
 */
@Slf4j
public abstract class BaseController {
    
    protected final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    /**
     * Create a successful response with data
     *
     * @param data    Response data
     * @param message Success message
     * @param <T>     Data type
     * @return ResponseEntity with success response
     */
    protected <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        log.info("Operation completed successfully: {}", message);
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }

    /**
     * Create a successful response with data and default message
     *
     * @param data Response data
     * @param <T>  Data type
     * @return ResponseEntity with success response
     */
    protected <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(data, "Operation completed successfully");
    }

    /**
     * Create a successful response for created resources
     *
     * @param data    Created resource data
     * @param message Success message
     * @param <T>     Data type
     * @return ResponseEntity with created status
     */
    protected <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        log.info("Resource created successfully: {}", message);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message, data));
    }

    /**
     * Create a successful response for created resources with default message
     *
     * @param data Created resource data
     * @param <T>  Data type
     * @return ResponseEntity with created status
     */
    protected <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return created(data, "Resource created successfully");
    }

    /**
     * Throw ResourceNotFoundException for missing resources
     * This will be handled by the global exception handler
     *
     * @param message Error message
     */
    protected void throwNotFound(String message) {
        throw new ResourceNotFoundException(message);
    }

    /**
     * Throw ResourceNotFoundException with default message
     *
     * @param entityName Name of the entity
     * @param id         ID of the entity
     */
    protected void throwNotFound(String entityName, Object id) {
        throw new ResourceNotFoundException(String.format("%s not found with ID: %s", entityName, id));
    }
}
