package com.qa.automation.service;

import com.qa.automation.model.UserPermission;
import com.qa.automation.repository.PermissionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public List<UserPermission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
