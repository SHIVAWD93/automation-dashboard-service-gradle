package com.qa.automation.controller;

import com.qa.automation.model.*;
import com.qa.automation.service.JenkinsService;
import com.qa.automation.service.JenkinsTestNGService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jenkins")
@RequiredArgsConstructor
@Slf4j
public class JenkinsController {


    private final JenkinsService jenkinsService;


    private final JenkinsTestNGService jenkinsTestNGService;


    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testJenkinsConnection() {
        try {
            log.info("Testing Jenkins connection");
            boolean connected = jenkinsService.testJenkinsConnection();
            Map<String, Object> response = new HashMap<>();
            response.put("connected", connected);
            response.put("message", connected ? "Successfully connected to Jenkins" : "Failed to connect to Jenkins");
            log.info("Jenkins connection test result: {}", connected ? "SUCCESS" : "FAILED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error testing Jenkins connection: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("connected", false);
            response.put("message", "Error testing connection: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/results")
    public ResponseEntity<List<JenkinsResult>> getAllLatestResults() {
        log.info("Fetching all latest Jenkins results");
        List<JenkinsResult> results = jenkinsService.getAllLatestResults();
        log.info("Retrieved {} Jenkins results", results.size());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/results/filtered")
    public ResponseEntity<List<JenkinsResult>> getFilteredResults(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long automationTesterId,
            @RequestParam(required = false) String jobFrequency,
            @RequestParam(required = false) String buildStatus) {
        try {
            List<JenkinsResult> results = jenkinsService.getAllLatestResults();

            // Apply filters
            List<JenkinsResult> filteredResults = results.stream()
                    .filter(result -> {
                        // Project filter
                        if (projectId != null && (result.getProject() == null || !result.getProject().getId().equals(projectId))) {
                            return false;
                        }
                        // Automation tester filter
                        if (automationTesterId != null && (result.getAutomationTester() == null ||
                                !result.getAutomationTester().getId().equals(automationTesterId))) {
                            return false;
                        }
                        // Job frequency filter
                        if (jobFrequency != null && !jobFrequency.isEmpty() && !jobFrequency.equalsIgnoreCase(result.getJobFrequency())) {
                            return false;
                        }
                        // Build status filter
                        if (buildStatus != null && !buildStatus.isEmpty() && !buildStatus.equalsIgnoreCase(result.getBuildStatus())) {
                            return false;
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            // Ensure job frequency is set for filtered results
            for (JenkinsResult result : filteredResults) {
                if (result.getJobFrequency() == null || result.getJobFrequency().isEmpty()) {
                    result.inferJobFrequency();
                }
            }

            return ResponseEntity.ok(filteredResults);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // NEW: Get unique job frequencies for filter dropdown
    @GetMapping("/frequencies")
    public ResponseEntity<List<String>> getJobFrequencies() {
        List<String> frequencies = jenkinsService.getJobFrequencies();
        return ResponseEntity.ok(frequencies);
    }

    // NEW: Get projects that have Jenkins results for filter dropdown
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjectsWithJenkinsResults() {
        List<Project> projects = jenkinsService.getProjectsWithJenkinsResults();
        return ResponseEntity.ok(projects);
    }

    // NEW: Get automation testers that have Jenkins results for filter dropdown
    @GetMapping("/automation-testers")
    public ResponseEntity<List<Tester>> getAutomationTestersWithJenkinsResults() {
        List<Tester> testers = jenkinsService.getAutomationTestersWithJenkinsResults();
        return ResponseEntity.ok(testers);
    }

    @GetMapping("/results/{jobName}")
    public ResponseEntity<JenkinsResult> getLatestResultByJobName(@PathVariable String jobName) {
        JenkinsResult result = jenkinsService.getLatestResultByJobName(jobName);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/results/{resultId}/testcases")
    public ResponseEntity<List<JenkinsTestCase>> getTestCasesByResultId(@PathVariable Long resultId) {
        List<JenkinsTestCase> testCases = jenkinsService.getTestCasesByResultId(resultId);
        return ResponseEntity.ok(testCases);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getJenkinsStatistics() {
        Map<String, Object> stats = jenkinsService.getJenkinsStatistics();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, String>> syncAllJobs() {
        try {
            jenkinsService.syncAllJobsFromJenkins();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Jenkins jobs synced successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to sync Jenkins jobs: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/sync/{jobName}")
    public ResponseEntity<Map<String, String>> syncJobResult(@PathVariable String jobName) {
        try {
            jenkinsService.syncJobResultFromJenkins(jobName);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Job " + jobName + " synced successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to sync job " + jobName + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // TestNG specific endpoints
    @GetMapping("/testng/report")
    public ResponseEntity<Map<String, Object>> generateTestNGReport() {
        try {
            Map<String, Object> report = jenkinsTestNGService.generateTestNGReport();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to generate TestNG report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/testng/{jobName}/{buildNumber}/testcases")
    public ResponseEntity<Map<String, Object>> getDetailedTestCases(
            @PathVariable String jobName,
            @PathVariable String buildNumber) {
        try {
            Map<String, Object> testCases = jenkinsTestNGService.getDetailedTestCases(jobName, buildNumber);
            return ResponseEntity.ok(testCases);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to get detailed test cases: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/testng/sync-and-report")
    public ResponseEntity<Map<String, Object>> syncAndGenerateReport() {
        try {
            // First sync all jobs
            jenkinsService.syncAllJobsFromJenkins();

            // Then generate the TestNG report
            Map<String, Object> report = jenkinsTestNGService.generateTestNGReport();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Sync completed and report generated successfully");
            response.put("report", report);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to sync and generate report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/results/{id}/notes")
    public ResponseEntity<Map<String, Object>> updateJenkinsResultNotes(
            @PathVariable Long id,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Handle different possible request formats
            String notes = null;
            if (requestBody.containsKey("bugsIdentified")) {
                notes = (String) requestBody.get("bugsIdentified");
            } else if (requestBody.containsKey("failureReasons")) {
                notes = (String) requestBody.get("failureReasons");
            } else if (requestBody.containsKey("notes")) {
                notes = (String) requestBody.get("notes");
            }

            JenkinsResult result = jenkinsService.updateJenkinsResultNotes(id, notes);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Notes updated successfully");
            response.put("id", id);
            response.put("notes", notes);
            response.put("timestamp", new Date());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update notes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/results/{id}/testers")
    public ResponseEntity<Map<String, Object>> assignTestersToJenkinsResult(
            @PathVariable Long id,
            @RequestBody TesterAssignmentRequest request) {
        try {
            JenkinsResult result = jenkinsService.assignTestersToJenkinsResult(
                    id, request.getAutomationTesterId(), request.getManualTesterId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Testers assigned successfully");
            response.put("id", id);
            response.put("automationTester", result.getAutomationTester() != null ?
                    result.getAutomationTester().getName() : null);
            response.put("manualTester", result.getManualTester() != null ?
                    result.getManualTester().getName() : null);
            response.put("passPercentage", result.getPassPercentage());
            response.put("timestamp", new Date());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to assign testers: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/results/{id}/save-all")
    public ResponseEntity<Map<String, Object>> saveAllData(
            @PathVariable Long id,
            @RequestBody CombinedSaveRequest request) {
        try {
            JenkinsResult result = jenkinsService.saveAllJenkinsResultData(
                    id, request.getNotes(), request.getAutomationTesterId(),
                    request.getManualTesterId(), request.getProjectId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Data saved successfully");
            response.put("id", id);
            response.put("notes", result.getBugsIdentified());
            response.put("automationTester", result.getAutomationTester() != null ?
                    result.getAutomationTester().getName() : null);
            response.put("manualTester", result.getManualTester() != null ?
                    result.getManualTester().getName() : null);
            response.put("project", result.getProject() != null ?
                    result.getProject().getName() : null);
            response.put("passPercentage", result.getPassPercentage());
            response.put("timestamp", new Date());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to save data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
