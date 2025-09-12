package com.qa.automation.controller;

import com.qa.automation.model.Tester;
import com.qa.automation.service.TesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/testers")
@RequiredArgsConstructor
public class TesterController extends BaseController {

    private final TesterService testerService;

    @GetMapping
    public ResponseEntity<List<Tester>> getAllTesters() {
        return executeWithErrorHandling(
                testerService::getAllTesters,
                "fetch all testers"
        );
    }

    @PostMapping
    public ResponseEntity<Tester> createTester(@RequestBody Tester tester) {
        return executeCreateOperation(
                () -> testerService.createTester(tester),
                "tester",
                tester.getName()
        );
    }

    @PostMapping(value = "with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Tester> createTesterWithImage(
            @ModelAttribute Tester tester,
            @RequestParam("profileImage") MultipartFile profileImage) {
        return executeCreateOperation(
                () -> testerService.createTester(tester, profileImage),
                "tester with image",
                tester.getName()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<Tester> getTesterById(@PathVariable Long id) {
        return executeGetByIdOperation(
                () -> testerService.getTesterById(id),
                "tester",
                id
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tester> updateTester(@PathVariable Long id, @RequestBody Tester tester) {
        return executeGetByIdOperation(
                () -> testerService.updateTester(id, tester),
                "update tester",
                id
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTester(@PathVariable Long id) {
        return executeDeleteOperation(
                () -> testerService.deleteTester(id),
                "delete tester",
                id
        );
    }
}
