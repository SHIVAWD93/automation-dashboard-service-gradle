package com.qa.automation.controller;

import com.qa.automation.dto.ApiResponse;
import com.qa.automation.service.DashboardService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController extends BaseController {


    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        logger.info("Fetching dashboard statistics");
        Map<String, Object> stats = dashboardService.getDashboardStats();
        return success(stats, "Dashboard statistics retrieved successfully");
    }

    @GetMapping("/stats/domain/{domainId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDomainStats(@PathVariable Long domainId) {
        logger.info("Fetching domain statistics for domain ID: {}", domainId);
        Map<String, Object> stats = dashboardService.getDomainStats(domainId);
        return success(stats, "Domain statistics retrieved successfully");
    }

    @GetMapping("/stats/project/{projectId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProjectStats(@PathVariable Long projectId) {
        logger.info("Fetching project statistics for project ID: {}", projectId);
        Map<String, Object> stats = dashboardService.getProjectStats(projectId);
        return success(stats, "Project statistics retrieved successfully");
    }
}
