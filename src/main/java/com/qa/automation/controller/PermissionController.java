package com.qa.automation.controller;

import com.qa.automation.model.UserPermission;
import com.qa.automation.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController extends BaseController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<List<UserPermission>> getAllPermissions() {
        return executeWithErrorHandling(
                permissionService::getAllPermissions,
                "fetch all permissions"
        );
    }
}

