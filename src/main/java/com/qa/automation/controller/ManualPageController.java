package com.qa.automation.controller;

import com.qa.automation.config.JiraConfig;
import com.qa.automation.dto.JiraIssueDto;
import com.qa.automation.dto.JiraTestCaseDto;
import com.qa.automation.model.*;
import com.qa.automation.service.JiraIntegrationService;
import com.qa.automation.service.ManualPageService;
import com.qa.automation.service.QTestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manual-page")
@RequiredArgsConstructor
public class ManualPageController {

    private static final Logger logger = LoggerFactory.getLogger(ManualPageController.class);


    private final ManualPageService manualPageService;


    private final JiraIntegrationService jiraIntegrationService;


    private final QTestService qTestService;


    private final JiraConfig jiraConfig;

    @GetMapping("/sprints")
    public ResponseEntity<List<Map<String, Object>>> getAvailableSprints(
            @RequestParam(required = false) String jiraProjectKey,
            @RequestParam(required = false) String jiraBoardId) {
        try {
            logger.info("Fetching available sprints (Project: {}, Board: {})", jiraProjectKey, jiraBoardId);
            List<Map<String, Object>> sprints = manualPageService.getAvailableSprints(jiraProjectKey, jiraBoardId);
            return ResponseEntity.ok(sprints);
        } catch (Exception e) {
            logger.error("Error fetching sprints: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/sprints/{sprintId}/sync")
    public ResponseEntity<List<JiraIssueDto>> syncSprintIssues(
            @PathVariable String sprintId,
            @RequestParam(required = false) String jiraProjectKey,
            @RequestParam(required = false) String jiraBoardId) {
        try {
            logger.info("Syncing issues for sprint: {} (Project: {}, Board: {})",
                    sprintId, jiraProjectKey, jiraBoardId);
            List<JiraIssueDto> issues = manualPageService.fetchAndSyncSprintIssues(
                    sprintId, jiraProjectKey, jiraBoardId);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            logger.error("Error fetching sprints: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/sprints/{sprintId}/issues")
    public ResponseEntity<List<JiraIssueDto>> getSprintIssues(@PathVariable String sprintId) {
        logger.info("Getting issues for sprint: {}", sprintId);
        List<JiraIssueDto> issues = manualPageService.getSprintIssues(sprintId);
        return ResponseEntity.ok(issues);
    }

    @PutMapping("/test-cases/{testCaseId}/automation-flags")
    public ResponseEntity<JiraTestCaseDto> updateAutomationFlags(
            @PathVariable("testCaseId") String testCaseIdStr,
            @RequestBody AutomationFlagsRequest request) {
        if (testCaseIdStr == null || testCaseIdStr.trim().isEmpty() || "null".equalsIgnoreCase(testCaseIdStr.trim())) {
            logger.warn("Received null/empty testCaseId in path: {}", testCaseIdStr);
            return ResponseEntity.badRequest().build();
        }
        Long testCaseId = Long.parseLong(testCaseIdStr.trim());
        logger.info("Updating automation flags for test case: {}", testCaseId);
        JiraTestCaseDto updatedTestCase = manualPageService.updateTestCaseAutomationFlags(
                testCaseId,
                request.isCanBeAutomated(),
                request.isCannotBeAutomated()
        );
        return ResponseEntity.ok(updatedTestCase);
    }

    @PutMapping("/test-cases/{testCaseId}/save")
    public ResponseEntity<JiraTestCaseDto> saveMappingAndFlags(
            @PathVariable("testCaseId") String testCaseIdStr,
            @RequestBody SaveTestCaseRequest request) {
        if (testCaseIdStr == null || testCaseIdStr.trim().isEmpty() || "null".equalsIgnoreCase(testCaseIdStr.trim())) {
            return ResponseEntity.badRequest().build();
        }
        Long testCaseId = Long.parseLong(testCaseIdStr.trim());

        // First map project/tester if provided
        JiraTestCaseDto dto = manualPageService.mapTestCaseToProject(testCaseId, request.getProjectId(), request.getTesterId());
        // Then update flags
        dto = manualPageService.updateTestCaseAutomationFlags(testCaseId, request.isCanBeAutomated(), request.isCannotBeAutomated());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/test-cases/{testCaseId}/mapping")
    public ResponseEntity<JiraTestCaseDto> mapTestCase(
            @PathVariable Long testCaseId,
            @RequestBody TestCaseMappingRequest request) {
        logger.info("Mapping test case {} to project {} and tester {}",
                testCaseId, request.getProjectId(), request.getTesterId());
        JiraTestCaseDto updatedTestCase = manualPageService.mapTestCaseToProject(
                testCaseId,
                request.getProjectId(),
                request.getTesterId()
        );
        return ResponseEntity.ok(updatedTestCase);
    }

    @PostMapping("/issues/{jiraKey}/keyword-search")
    public ResponseEntity<JiraIssueDto> searchKeywordInComments(
            @PathVariable String jiraKey,
            @RequestBody KeywordSearchRequest request) {
        logger.info("Searching for keyword '{}' in issue: {}", request.getKeyword(), jiraKey);
        JiraIssueDto updatedIssue = manualPageService.searchKeywordInIssue(jiraKey, request.getKeyword());
        return ResponseEntity.ok(updatedIssue);
    }

    @PostMapping("/global-keyword-search")
    public ResponseEntity<Map<String, Object>> globalKeywordSearch(
            @RequestBody GlobalKeywordSearchRequest request) {
        logger.info("Performing global keyword search for '{}' in project: {} sprint: {}",
                request.getKeyword(), request.getJiraProjectKey(), request.getSprintId());
        Map<String, Object> searchResults = jiraIntegrationService.searchKeywordGlobally(
                request.getKeyword(), request.getJiraProjectKey(), request.getSprintId());
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/sprints/{sprintId}/statistics")
    public ResponseEntity<Map<String, Object>> getSprintStatistics(@PathVariable String sprintId) {
        logger.info("Getting automation statistics for sprint: {}", sprintId);
        Map<String, Object> statistics = manualPageService.getSprintAutomationStatistics(sprintId);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        logger.info("Getting all projects for mapping");
        List<Project> projects = manualPageService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/domains")
    public ResponseEntity<List<Domain>> getAllDomains() {
        logger.info("Getting all domains for filtering");
        List<Domain> domains = manualPageService.getAllDomains();
        return ResponseEntity.ok(domains);
    }

    @GetMapping("/testers")
    public ResponseEntity<List<Tester>> getAllTesters() {
        logger.info("Getting all testers for assignment");
        List<Tester> testers = manualPageService.getAllTesters();
        return ResponseEntity.ok(testers);
    }

    @GetMapping("/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        try {
            logger.info("Testing Jira connection");
            boolean isConnected = jiraIntegrationService.testConnection();
            Map<String, Object> response = Map.of(
                    "connected", isConnected,
                    "message", isConnected ? "Successfully connected to Jira" : "Failed to connect to Jira"
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error testing connection: {}", e.getMessage(), e);
            Map<String, Object> response = Map.of(
                    "connected", false,
                    "message", "Error testing connection: " + e.getMessage()
            );
            return ResponseEntity.ok(response);
        }
    }
}
