package com.qa.automation.controller;

import com.qa.automation.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController extends BaseController {


    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return executeWithErrorHandling(
                dashboardService::getDashboardStats,
                "fetch dashboard statistics"
        );
    }

    @GetMapping("/stats/domain/{domainId}")
    public ResponseEntity<Map<String, Object>> getDomainStats(@PathVariable Long domainId) {
        return executeWithErrorHandling(
                () -> dashboardService.getDomainStats(domainId),
                "fetch domain statistics for ID: " + domainId
        );
    }

    @GetMapping("/stats/project/{projectId}")
    public ResponseEntity<Map<String, Object>> getProjectStats(@PathVariable Long projectId) {
        return executeWithErrorHandling(
                () -> dashboardService.getProjectStats(projectId),
                "fetch project statistics for ID: " + projectId
        );
    }
}
