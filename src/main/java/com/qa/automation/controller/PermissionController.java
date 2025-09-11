package com.qa.automation.controller;

import com.qa.automation.dto.ApiResponse;
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
public class PermissionController extends BaseController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserPermission>>> getAllPermissions() {
        logger.info("Fetching all permissions");
        List<UserPermission> permissions = permissionService.getAllPermissions();
        return success(permissions, "Permissions retrieved successfully");
    }
}

