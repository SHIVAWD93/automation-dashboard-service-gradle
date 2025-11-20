package com.qa.automation.service;

import com.qa.automation.model.*;
import com.qa.automation.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Centralized service for managing all lookup tables
 * Provides methods for finding and creating lookup values
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LookupService {

    private final PriorityRepository priorityRepository;
    private final ToolTypeRepository toolTypeRepository;
    private final TestCaseTypeRepository testCaseTypeRepository;
    private final WorkflowStatusRepository workflowStatusRepository;
    private final AutomationStatusRepository automationStatusRepository;
    private final BuildStatusRepository buildStatusRepository;
    private final RoleRepository roleRepository;

    // ==================== PRIORITY ====================

    public Priority findOrCreatePriority(String code) {
        if (code == null || code.trim().isEmpty()) {
            return getDefaultPriority();
        }
        return priorityRepository.findByCodeIgnoreCase(code.trim())
                .orElseGet(() -> {
                    log.info("New priority not found with code: {}", code);
                    return null;
                });
    }

    public Optional<Priority> findPriorityByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        return priorityRepository.findByCodeIgnoreCase(code.trim());
    }

    public List<Priority> getAllActivePriorities() {
        return priorityRepository.findByCodeStatusOrderBySortBy("Active");
    }

    private Priority getDefaultPriority() {
        return findOrCreatePriority("Medium");
    }

    // ==================== TOOL TYPE ====================

    public ToolType findOrCreateToolType(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        return toolTypeRepository.findByCodeIgnoreCase(code.trim())
                .orElseGet(() -> {
                    log.info("New tool type not found with code: {}", code);
                    return null;
                });
    }

    public Optional<ToolType> findToolTypeByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        return toolTypeRepository.findByCodeIgnoreCase(code.trim());
    }

    public List<ToolType> getAllActiveToolTypes() {
        return toolTypeRepository.findByCodeStatusOrderBySortBy("Active");
    }

    // ==================== TEST CASE TYPE ====================

    public TestCaseType findOrCreateTestCaseType(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        return testCaseTypeRepository.findByCodeIgnoreCase(code.trim())
                .orElseGet(() -> {
                    log.info("New test case type not found with code: {}", code);
                    return null;
                });
    }

    public Optional<TestCaseType> findTestCaseTypeByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        return testCaseTypeRepository.findByCodeIgnoreCase(code.trim());
    }

    public List<TestCaseType> getAllActiveTestCaseTypes() {
        return testCaseTypeRepository.findByCodeStatusOrderBySortBy("Active");
    }

    // ==================== WORKFLOW STATUS ====================

    public WorkflowStatus findOrCreateWorkflowStatus(String code) {
        if (code == null || code.trim().isEmpty()) {
            return getDefaultWorkflowStatus();
        }

        return workflowStatusRepository.findByCodeIgnoreCase(code.trim())
                .orElseGet(() -> {
                    log.info("New workflow status not found with code: {}", code);
                    return null;
                });
    }

    public Optional<WorkflowStatus> findWorkflowStatusByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        return workflowStatusRepository.findByCodeIgnoreCase(code.trim());
    }

    public List<WorkflowStatus> getAllActiveWorkflowStatuses() {
        return workflowStatusRepository.findByCodeStatusOrderBySortBy("Active");
    }

    private WorkflowStatus getDefaultWorkflowStatus() {
        return findOrCreateWorkflowStatus("Active");
    }

    // ==================== AUTOMATION STATUS ====================

    public AutomationStatus findOrCreateAutomationStatus(String code) {
        if (code == null || code.trim().isEmpty()) {
            return getDefaultAutomationStatus();
        }
        return automationStatusRepository.findByCodeIgnoreCase(code.trim())
                .orElseGet(() -> {
                    log.info("New automation status not found with code: {}", code);
                    return null;
                });
    }

    public Optional<AutomationStatus> findAutomationStatusByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        return automationStatusRepository.findByCodeIgnoreCase(code.trim());
    }

    public List<AutomationStatus> getAllActiveAutomationStatuses() {
        return automationStatusRepository.findByCodeStatusOrderBySortBy("Active");
    }

    private AutomationStatus getDefaultAutomationStatus() {
        return findOrCreateAutomationStatus("PENDING");
    }

    public AutomationStatus determineAutomationStatus(boolean canBeAutomated, boolean cannotBeAutomated) {
        if (canBeAutomated && !cannotBeAutomated) {
            return findOrCreateAutomationStatus("READY_TO_AUTOMATE");
        } else if (!canBeAutomated && cannotBeAutomated) {
            return findOrCreateAutomationStatus("NOT_AUTOMATABLE");
        } else {
            return findOrCreateAutomationStatus("PENDING");
        }
    }

    // ==================== BUILD STATUS ====================

    public BuildStatus findOrCreateBuildStatus(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }

        return buildStatusRepository.findByCodeIgnoreCase(code.trim())
                .orElseGet(() -> {
                    log.info("New build status not found with code: {}", code);
                    return null;
                });
    }

    public Optional<BuildStatus> findBuildStatusByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        return buildStatusRepository.findByCodeIgnoreCase(code.trim());
    }

    public List<BuildStatus> getAllActiveBuildStatuses() {
        return buildStatusRepository.findByCodeStatusOrderBySortBy("Active");
    }

    // ==================== ROLE ====================

    public Role findOrCreateRole(String code) {
        if (code == null || code.trim().isEmpty()) {
            return getDefaultRole();
        }

        return roleRepository.findByCodeIgnoreCase(code.trim())
                .orElseGet(() -> {
                    log.info("New role not found with code: {}", code);
                    return null;
                });
    }

    public Optional<Role> findRoleByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        return roleRepository.findByCodeIgnoreCase(code.trim());
    }

    public List<Role> getAllActiveRoles() {
        return roleRepository.findByCodeStatusOrderBySortBy("Active");
    }

    private Role getDefaultRole() {
        return findOrCreateRole("Tester");
    }

}