package com.qa.automation.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Base controller providing common functionality for all controllers
 */
@Slf4j
public abstract class BaseController {

    protected final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    /**
     * Execute an operation with standardized error handling and logging
     *
     * @param operation     The operation to execute
     * @param operationName Name of the operation for logging
     * @param <T>           Return type
     * @return ResponseEntity with result or error
     */
    protected <T> ResponseEntity<T> executeWithErrorHandling(
            Supplier<T> operation,
            String operationName) {
        try {
            log.info("Executing operation: {}", operationName);
            T result = operation.get();
            log.info("Successfully completed operation: {}", operationName);
            return ResponseEntity.ok(result);
        }
        catch (RuntimeException e) {
            log.warn("Business logic error in operation {}: {}", operationName, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            log.error("Error in operation {}: {}", operationName, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Execute an operation that returns a boolean (typically delete operations)
     *
     * @param operation     The operation to execute
     * @param operationName Name of the operation for logging
     * @param entityId      ID of the entity being operated on
     * @return ResponseEntity with appropriate status
     */
    protected ResponseEntity<Void> executeDeleteOperation(
            Supplier<Boolean> operation,
            String operationName,
            Object entityId) {
        try {
            log.info("Executing delete operation: {} for ID: {}", operationName, entityId);
            boolean result = operation.get();
            if (result) {
                log.info("Successfully completed delete operation: {} for ID: {}", operationName, entityId);
                return ResponseEntity.noContent().build();
            }
            log.warn("Entity not found for delete operation: {} with ID: {}", operationName, entityId);
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            log.error("Error in delete operation {} for ID {}: {}", operationName, entityId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Execute a create operation with standardized error handling
     *
     * @param operation     The operation to execute
     * @param operationName Name of the operation for logging
     * @param entityName    Name of the entity being created
     * @param <T>           Return type
     * @return ResponseEntity with created entity or error
     */
    protected <T> ResponseEntity<T> executeCreateOperation(
            Supplier<T> operation,
            String operationName,
            String entityName) {
        try {
            log.info("Creating {}: {}", operationName, entityName);
            T result = operation.get();
            log.info("Successfully created {}: {}", operationName, entityName);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        catch (RuntimeException e) {
            log.warn("Failed to create {}: {}", operationName, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            log.error("Error creating {}: {}", operationName, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Execute an operation that may return null (typically get by ID operations)
     *
     * @param operation     The operation to execute
     * @param operationName Name of the operation for logging
     * @param entityId      ID of the entity being retrieved
     * @param <T>           Return type
     * @return ResponseEntity with entity or not found status
     */
    protected <T> ResponseEntity<T> executeGetByIdOperation(
            Supplier<T> operation,
            String operationName,
            Object entityId) {
        try {
            log.info("Fetching {} by ID: {}", operationName, entityId);
            T result = operation.get();
            if (result != null) {
                return ResponseEntity.ok(result);
            }
            log.warn("{} not found with ID: {}", operationName, entityId);
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            log.error("Error fetching {} by ID {}: {}", operationName, entityId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Create a standardized error response
     *
     * @param message Error message
     * @param status  HTTP status
     * @return ResponseEntity with error details
     */
    protected ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Create a standardized success response
     *
     * @param message Success message
     * @param data    Response data
     * @return ResponseEntity with success details
     */
    protected ResponseEntity<Map<String, Object>> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
