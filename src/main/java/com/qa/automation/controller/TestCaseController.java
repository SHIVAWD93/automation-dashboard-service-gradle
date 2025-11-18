package com.qa.automation.controller;

import com.qa.automation.model.TestCase;
import com.qa.automation.service.TestCaseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
@Slf4j
//@PreAuthorize(value="@amsHelper.hasGlobalPermission('automation-dashboard.read')")
public class TestCaseController {

    private final TestCaseService testCaseService;

    @GetMapping
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
            "'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<List<TestCase>> getAllTestCases() {
        log.info("Fetching all test cases");
        List<TestCase> testCases = testCaseService.getAllTestCases();
        log.info("Retrieved {} test cases", testCases.size());
        return ResponseEntity.ok(testCases);
    }

    @PostMapping
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<?> createTestCase(@RequestBody TestCase testCase) {
        log.info("Creating new test case: {}", testCase.getTitle());
        TestCase savedTestCase = testCaseService.createTestCase(testCase);
        log.info("Successfully created test case with ID: {}", savedTestCase.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTestCase);
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
            "'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<TestCase> getTestCaseById(@PathVariable Long id) {
        TestCase testCase = testCaseService.getTestCaseById(id);
        if (testCase != null) {
            return ResponseEntity.ok(testCase);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<?> updateTestCase(@PathVariable Long id, @RequestBody TestCase testCase) {
        TestCase updatedTestCase = testCaseService.updateTestCase(id, testCase);
        if (updatedTestCase != null) {
            return ResponseEntity.ok(updatedTestCase);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        boolean deleted = testCaseService.deleteTestCase(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
            "'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<List<TestCase>> getTestCasesByProject(@PathVariable Long projectId) {
        List<TestCase> testCases = testCaseService.getTestCasesByProject(projectId);
        return ResponseEntity.ok(testCases);
    }

    @GetMapping("/domain/{domainId}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
            "'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<List<TestCase>> getTestCasesByDomain(@PathVariable Long domainId) {
        List<TestCase> testCases = testCaseService.getTestCasesByDomain(domainId);
        return ResponseEntity.ok(testCases);
    }

}
