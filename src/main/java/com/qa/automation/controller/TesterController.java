package com.qa.automation.controller;

import com.qa.automation.dto.TesterDto;
import com.qa.automation.model.Tester;
import com.qa.automation.service.TesterService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
            "'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<List<Tester>> getAllTesters() {
        return executeWithErrorHandling(
                testerService::getAllTesters,
                "fetch all testers"
        );
    }

    @PostMapping
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.admin'})")
    public ResponseEntity<Tester> createTester(@RequestBody Tester tester) {
        return executeCreateOperation(
                () -> testerService.createTester(tester),
                "tester",
                tester.getName()
        );
    }

    @PostMapping(value = "with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.admin'})")
    public ResponseEntity<Tester> createTesterWithImage(
            @ModelAttribute TesterDto tester,
            @RequestParam("profileImage") MultipartFile profileImage) {
        return executeCreateOperation(
                () -> testerService.createTester(tester, profileImage),
                "tester with image",
                tester.getName()
        );
    }


    @GetMapping("/{id}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.read'," +
            "'automation-dashboard.write','automation-dashboard.admin'})")
    public ResponseEntity<Tester> getTesterById(@PathVariable Long id) {
        return executeGetByIdOperation(
                () -> testerService.getTesterById(id),
                "tester",
                id
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.admin'})")
    public ResponseEntity<Tester> updateTester(@PathVariable Long id, @RequestBody Tester tester) {
        return executeGetByIdOperation(
                () -> testerService.updateTester(id, tester),
                "update tester",
                id
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission(new String[]{'automation-dashboard.admin'})")
    public ResponseEntity<Void> deleteTester(@PathVariable Long id) {
        return executeDeleteOperation(
                () -> testerService.deleteTester(id),
                "delete tester",
                id
        );
    }
}
