package com.qa.automation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JiraTestCaseDto {
    private Long id;
    private String qtestTitle;
    private String qtestId;
    private String qtestAssignee;
    private String qtestAssigneeDisplayName;
    private String qtestPriority;
    private String qtestAutomationStatus;
    private Boolean canBeAutomated;
    private Boolean cannotBeAutomated;

    // Changed to string for backward compatibility
    private String automationStatus;

    private Long assignedTesterId;
    private String assignedTesterName;
    private Long projectId;
    private String projectName;
    private String domainMapped;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Changed to strings for backward compatibility
    private String testCaseType;
    private String toolType;

    private Long manualTesterId;
    private String manualTesterName;

    public JiraTestCaseDto(String qtestTitle) {
        this.qtestTitle = qtestTitle;
        this.canBeAutomated = false;
        this.cannotBeAutomated = false;
        this.automationStatus = "PENDING";
    }
}