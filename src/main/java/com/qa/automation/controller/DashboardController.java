package com.qa.automation.controller;

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
public class DashboardController {


    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/domain/{domainId}")
    public ResponseEntity<Map<String, Object>> getDomainStats(@PathVariable Long domainId) {
        Map<String, Object> stats = dashboardService.getDomainStats(domainId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/project/{projectId}")
    public ResponseEntity<Map<String, Object>> getProjectStats(@PathVariable Long projectId) {
        Map<String, Object> stats = dashboardService.getProjectStats(projectId);
        return ResponseEntity.ok(stats);
    }
}
