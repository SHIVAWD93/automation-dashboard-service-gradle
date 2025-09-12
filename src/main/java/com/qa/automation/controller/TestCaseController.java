package com.qa.automation.controller;

import com.qa.automation.model.TestCase;
import com.qa.automation.service.TestCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
@Slf4j
public class TestCaseController {

    private final TestCaseService testCaseService;

    @GetMapping
    public ResponseEntity<List<TestCase>> getAllTestCases() {
        log.info("Fetching all test cases");
        List<TestCase> testCases = testCaseService.getAllTestCases();
        log.info("Retrieved {} test cases", testCases.size());
        return ResponseEntity.ok(testCases);
    }

    @PostMapping
    public ResponseEntity<?> createTestCase(@RequestBody TestCase testCase) {
        log.info("Creating new test case: {}", testCase.getTitle());
        TestCase savedTestCase = testCaseService.createTestCase(testCase);
        log.info("Successfully created test case with ID: {}", savedTestCase.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTestCase);
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
        TestCase updatedTestCase = testCaseService.updateTestCase(id, testCase);
        if (updatedTestCase != null) {
            return ResponseEntity.ok(updatedTestCase);
        }
        return ResponseEntity.notFound().build();
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
