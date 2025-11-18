package com.qa.automation.controller;

import com.qa.automation.service.DashboardService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
            "'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {

        return executeWithErrorHandling(
                dashboardService::getDashboardStats,
                "fetch dashboard statistics"
        );
    }

    @GetMapping("/stats/domain/{domainId}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
            "'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<Map<String, Object>> getDomainStats(@PathVariable Long domainId) {
        return executeWithErrorHandling(
                () -> dashboardService.getDomainStats(domainId),
                "fetch domain statistics for ID: " + domainId
        );
    }

    @GetMapping("/stats/project/{projectId}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
            "'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<Map<String, Object>> getProjectStats(@PathVariable Long projectId) {
        return executeWithErrorHandling(
                () -> dashboardService.getProjectStats(projectId),
                "fetch project statistics for ID: " + projectId
        );
    }
}
