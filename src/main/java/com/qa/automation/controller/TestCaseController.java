package com.qa.automation.controller;

import com.qa.automation.model.TestCase;
import com.qa.automation.service.TestCaseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class TestCaseController {

    private final TestCaseService testCaseService;

    @GetMapping
    public ResponseEntity<List<TestCase>> getAllTestCases() {
        try {
            log.info("Fetching all test cases");
            List<TestCase> testCases = testCaseService.getAllTestCases();
            log.info("Retrieved {} test cases", testCases.size());
            return ResponseEntity.ok(testCases);
        }
        catch (Exception e) {
            log.error("Error fetching all test cases: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createTestCase(@RequestBody TestCase testCase) {
        try {
            log.info("Creating new test case: {}", testCase.getTitle());
            TestCase savedTestCase = testCaseService.createTestCase(testCase);
            log.info("Successfully created test case with ID: {}", savedTestCase.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTestCase);
        }
        catch (RuntimeException e) {
            log.warn("Failed to create test case: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            log.error("Error creating test case: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCase> getTestCaseById(@PathVariable Long id) {
        TestCase testCase = testCaseService.getTestCaseById(id);
        if (testCase != null) {
            return ResponseEntity.ok(testCase);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTestCase(@PathVariable Long id, @RequestBody TestCase testCase) {
        try {
            TestCase updatedTestCase = testCaseService.updateTestCase(id, testCase);
            if (updatedTestCase != null) {
                return ResponseEntity.ok(updatedTestCase);
            }
            return ResponseEntity.notFound().build();
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable Long id) {
        boolean deleted = testCaseService.deleteTestCase(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TestCase>> getTestCasesByProject(@PathVariable Long projectId) {
        List<TestCase> testCases = testCaseService.getTestCasesByProject(projectId);
        return ResponseEntity.ok(testCases);
    }

    @GetMapping("/domain/{domainId}")
    public ResponseEntity<List<TestCase>> getTestCasesByDomain(@PathVariable Long domainId) {
        List<TestCase> testCases = testCaseService.getTestCasesByDomain(domainId);
        return ResponseEntity.ok(testCases);
    }

}
