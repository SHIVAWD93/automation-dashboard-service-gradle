package com.qa.automation.controller;

import com.qa.automation.model.UserPermission;
import com.qa.automation.service.PermissionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    ResponseEntity<List<UserPermission>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }
}

