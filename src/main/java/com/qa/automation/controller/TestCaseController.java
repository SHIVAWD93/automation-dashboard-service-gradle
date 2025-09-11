package com.qa.automation.controller;

import com.qa.automation.model.TestCase;
import com.qa.automation.service.TestCaseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/testcases")
@RequiredArgsConstructor
public class TestCaseController {

    private final TestCaseService testCaseService;

    @GetMapping
    public ResponseEntity<List<TestCase>> getAllTestCases() {
        List<TestCase> testCases = testCaseService.getAllTestCases();
        return ResponseEntity.ok(testCases);
    }

    @PostMapping
    public ResponseEntity<?> createTestCase(@RequestBody TestCase testCase) {
        try {
            TestCase savedTestCase = testCaseService.createTestCase(testCase);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTestCase);
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
