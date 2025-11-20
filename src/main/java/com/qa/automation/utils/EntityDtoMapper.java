package com.qa.automation.utils;

import com.qa.automation.dto.*;
import com.qa.automation.model.*;
import org.springframework.stereotype.Component;

/**
 * Utility class for converting between entities and DTOs
 * Maintains backward compatibility by converting lookup table references to string codes
 */
@Component
public class EntityDtoMapper {

    // ==================== TEST CASE ====================

    public TestCaseDto toDto(TestCase entity) {
        if (entity == null) {
            return null;
        }

        TestCaseDto dto = new TestCaseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());

        // Convert lookup references to codes
        dto.setPriority(entity.getPriority() != null ? entity.getPriority().getCode() : null);
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        dto.setTestCaseType(entity.getTestCaseType() != null ? entity.getTestCaseType().getCode() : null);
        dto.setToolType(entity.getToolType() != null ? entity.getToolType().getCode() : null);

        // Set related entities
        dto.setProjectId(entity.getProject() != null ? entity.getProject().getId() : null);
        dto.setProjectName(entity.getProject() != null ? entity.getProject().getName() : null);
        dto.setTesterId(entity.getTester() != null ? entity.getTester().getId() : null);
        dto.setTesterName(entity.getTester() != null ? entity.getTester().getName() : null);
        dto.setManualTesterId(entity.getManualTester() != null ? entity.getManualTester().getId() : null);
        dto.setManualTesterName(entity.getManualTester() != null ? entity.getManualTester().getName() : null);

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    // ==================== JIRA TEST CASE ====================

    public JiraTestCaseDto toDto(JiraTestCase entity) {
        if (entity == null) {
            return null;
        }

        JiraTestCaseDto dto = new JiraTestCaseDto();
        dto.setId(entity.getId());
        dto.setQtestTitle(entity.getQtestTitle());
        dto.setQtestId(entity.getQtestId());
        dto.setQtestAssignee(entity.getQtestAssignee());
        dto.setQtestPriority(entity.getQtestPriority());
        dto.setQtestAutomationStatus(entity.getQtestAutomationStatus());
        dto.setCanBeAutomated(entity.getCanBeAutomated());
        dto.setCannotBeAutomated(entity.getCannotBeAutomated());

        // Convert automation status to code
        dto.setAutomationStatus(entity.getAutomationStatus() != null ?
                entity.getAutomationStatus().getCode() : null);

        dto.setAssignedTesterId(entity.getAssignedTesterId());
        dto.setProjectId(entity.getProject() != null ? entity.getProject().getId() : null);
        dto.setProjectName(entity.getProject() != null ? entity.getProject().getName() : null);
        dto.setDomainMapped(entity.getDomainMapped());
        dto.setNotes(entity.getNotes());

        if (entity.getAssignedTester() != null) {
            dto.setAssignedTesterName(entity.getAssignedTester().getName());
        }

        // Convert test case type and tool type to codes
        dto.setTestCaseType(entity.getTestCaseType() != null ? entity.getTestCaseType().getCode() : null);
        dto.setToolType(entity.getToolType() != null ? entity.getToolType().getCode() : null);

        dto.setManualTesterId(entity.getManualTester() != null ? entity.getManualTester().getId() : null);
        dto.setManualTesterName(entity.getManualTester() != null ? entity.getManualTester().getName() : null);

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    // ==================== TESTER ====================

    public TesterDto toDto(Tester entity) {
        if (entity == null) {
            return null;
        }

        TesterDto dto = new TesterDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        // Convert role to code
        dto.setRole(entity.getRole() != null ? entity.getRole().getCode() : null);

        dto.setGender(entity.getGender());
        dto.setExperience(entity.getExperience());
        dto.setProfileImageUrl(entity.getProfileImageUrl());

        return dto;
    }

    // ==================== DOMAIN ====================

    public DomainDto toDto(Domain entity) {
        if (entity == null) {
            return null;
        }

        DomainDto dto = new DomainDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        // Convert status to code
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    // ==================== PROJECT ====================

    public ProjectDto toDto(Project entity) {
        if (entity == null) {
            return null;
        }

        ProjectDto dto = new ProjectDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        // Convert status to code
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);

        dto.setDomainId(entity.getDomain() != null ? entity.getDomain().getId() : null);
        dto.setDomainName(entity.getDomain() != null ? entity.getDomain().getName() : null);
        dto.setJiraProjectKey(entity.getJiraProjectKey());
        dto.setJiraBoardId(entity.getJiraBoardId());

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    // ==================== JENKINS RESULT ====================

    public JenkinsResultDto toDto(JenkinsResult entity) {
        if (entity == null) {
            return null;
        }

        JenkinsResultDto dto = new JenkinsResultDto();
        dto.setId(entity.getId());
        dto.setJobName(entity.getJobName());
        dto.setBuildNumber(entity.getBuildNumber());

        // Convert build status to code
        dto.setBuildStatus(entity.getBuildStatus() != null ? entity.getBuildStatus().getCode() : null);

        dto.setBuildUrl(entity.getBuildUrl());
        dto.setBuildTimestamp(entity.getBuildTimestamp());
        dto.setTotalTests(entity.getTotalTests());
        dto.setPassedTests(entity.getPassedTests());
        dto.setFailedTests(entity.getFailedTests());
        dto.setSkippedTests(entity.getSkippedTests());
        dto.setPassPercentage(entity.getPassPercentage());
        dto.setBugsIdentified(entity.getBugsIdentified());
        dto.setFailureReasons(entity.getFailureReasons());
        dto.setJobFrequency(entity.getJobFrequency());

        dto.setAutomationTesterId(entity.getAutomationTester() != null ?
                entity.getAutomationTester().getId() : null);
        dto.setAutomationTesterName(entity.getAutomationTester() != null ?
                entity.getAutomationTester().getName() : null);
        dto.setManualTesterId(entity.getManualTester() != null ?
                entity.getManualTester().getId() : null);
        dto.setManualTesterName(entity.getManualTester() != null ?
                entity.getManualTester().getName() : null);
        dto.setProjectId(entity.getProject() != null ? entity.getProject().getId() : null);
        dto.setProjectName(entity.getProject() != null ? entity.getProject().getName() : null);

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}