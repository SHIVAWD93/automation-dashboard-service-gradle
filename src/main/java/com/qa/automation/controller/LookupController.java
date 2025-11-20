package com.qa.automation.controller;

import com.qa.automation.model.*;
import com.qa.automation.service.LookupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller for managing lookup table values
 * Provides endpoints to retrieve available options for dropdowns
 */
@RestController
@RequestMapping("/api/lookups")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
        "'automation-dashboard.write','automation-dashboard.admin'})")
public class LookupController extends BaseController {

    private final LookupService lookupService;

    /**
     * Get all active priorities
     */
    @GetMapping("/priorities")
    public ResponseEntity<List<Priority>> getAllPriorities() {
        return executeWithErrorHandling(
                lookupService::getAllActivePriorities,
                "fetch all priorities"
        );
    }

    /**
     * Get all active tool types
     */
    @GetMapping("/tool-types")
    public ResponseEntity<List<ToolType>> getAllToolTypes() {
        return executeWithErrorHandling(
                lookupService::getAllActiveToolTypes,
                "fetch all tool types"
        );
    }

    /**
     * Get all active test case types
     */
    @GetMapping("/test-case-types")
    public ResponseEntity<List<TestCaseType>> getAllTestCaseTypes() {
        return executeWithErrorHandling(
                lookupService::getAllActiveTestCaseTypes,
                "fetch all test case types"
        );
    }

    /**
     * Get all active workflow statuses
     */
    @GetMapping("/workflow-statuses")
    public ResponseEntity<List<WorkflowStatus>> getAllWorkflowStatuses() {
        return executeWithErrorHandling(
                lookupService::getAllActiveWorkflowStatuses,
                "fetch all workflow statuses"
        );
    }

    /**
     * Get all active automation statuses
     */
    @GetMapping("/automation-statuses")
    public ResponseEntity<List<AutomationStatus>> getAllAutomationStatuses() {
        return executeWithErrorHandling(
                lookupService::getAllActiveAutomationStatuses,
                "fetch all automation statuses"
        );
    }

    /**
     * Get all active build statuses
     */
    @GetMapping("/build-statuses")
    public ResponseEntity<List<BuildStatus>> getAllBuildStatuses() {
        return executeWithErrorHandling(
                lookupService::getAllActiveBuildStatuses,
                "fetch all build statuses"
        );
    }

    /**
     * Get all active roles
     */
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return executeWithErrorHandling(
                lookupService::getAllActiveRoles,
                "fetch all roles"
        );
    }

}