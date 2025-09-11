package com.qa.automation.controller;

import com.qa.automation.dto.ApiResponse;
import com.qa.automation.model.Tester;
import com.qa.automation.service.TesterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/testers")
@RequiredArgsConstructor
public class TesterController extends BaseController {

    private final TesterService testerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Tester>>> getAllTesters() {
        logger.info("Fetching all testers");
        List<Tester> testers = testerService.getAllTesters();
        return success(testers, "Testers retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Tester>> createTester(@RequestBody Tester tester) {
        logger.info("Creating new tester: {}", tester.getName());
        Tester savedTester = testerService.createTester(tester);
        return created(savedTester, "Tester created successfully");
    }

    @PostMapping(value = "with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Tester>> createTesterWithImage(
            @ModelAttribute Tester tester,
            @RequestParam("profileImage") MultipartFile profileImage) {
        logger.info("Creating new tester with image: {}", tester.getName());
        Tester savedTester = testerService.createTester(tester, profileImage);
        return created(savedTester, "Tester with image created successfully");
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Tester>> getTesterById(@PathVariable Long id) {
        logger.info("Fetching tester by ID: {}", id);
        Tester tester = testerService.getTesterById(id);
        if (tester == null) {
            throwNotFound("Tester", id);
        }
        return success(tester, "Tester retrieved successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Tester>> updateTester(@PathVariable Long id, @RequestBody Tester tester) {
        logger.info("Updating tester with ID: {}", id);
        Tester updatedTester = testerService.updateTester(id, tester);
        if (updatedTester == null) {
            throwNotFound("Tester", id);
        }
        return success(updatedTester, "Tester updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTester(@PathVariable Long id) {
        logger.info("Deleting tester with ID: {}", id);
        boolean deleted = testerService.deleteTester(id);
        if (!deleted) {
            throwNotFound("Tester", id);
        }
        return ResponseEntity.noContent().build();
    }
}
