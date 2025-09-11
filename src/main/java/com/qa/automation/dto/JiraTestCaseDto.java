package com.qa.automation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String automationStatus;
    private Long assignedTesterId;
    private String assignedTesterName;
    private Long projectId;
    private String projectName;
    private String domainMapped;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JiraTestCaseDto(String qtestTitle) {
        this.qtestTitle = qtestTitle;
        this.canBeAutomated = false;
        this.cannotBeAutomated = false;
        this.automationStatus = "PENDING";
    }
}
