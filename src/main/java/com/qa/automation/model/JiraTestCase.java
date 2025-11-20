package com.qa.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "jira_test_cases")
@Data
public class JiraTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qtest_title", nullable = false)
    private String qtestTitle;

    @Column(name = "qtest_id")
    private String qtestId;

    @Column(name = "qtest_assignee")
    private String qtestAssignee;

    @Column(name = "qtest_priority")
    private String qtestPriority;

    @Column(name = "qtest_automation_status")
    private String qtestAutomationStatus;

    @Column(name = "can_be_automated", nullable = false)
    private Boolean canBeAutomated = false;

    @Column(name = "cannot_be_automated", nullable = false)
    private Boolean cannotBeAutomated = false;

    // Updated to use AutomationStatus lookup table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "automation_status_id")
    private AutomationStatus automationStatus;

    @Column(name = "assigned_tester_id")
    private Long assignedTesterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jira_issue_id", nullable = false)
    @JsonIgnoreProperties("linkedTestCases")
    private JiraIssue jiraIssue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties({"testCases", "domain"})
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tester_id")
    @JsonIgnoreProperties("testCases")
    private Tester assignedTester;

    // Updated to use TestCaseType lookup table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_case_type_id")
    private TestCaseType testCaseType;

    // Updated to use ToolType lookup table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tool_type_id")
    private ToolType toolType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manual_tester_id")
    @JsonIgnoreProperties("testCases")
    private Tester manualTester;

    @Column(name = "domain_mapped")
    private String domainMapped;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        updateAutomationStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateAutomationStatus();
    }

    private void updateAutomationStatus() {
        // This will be handled by the service layer with proper lookup
    }

    public void setCannotBeAutomated(Boolean cannotBeAutomated) {
        this.cannotBeAutomated = cannotBeAutomated;
        updateAutomationStatus();
    }

    public void setAssignedTester(Tester assignedTester) {
        this.assignedTester = assignedTester;
        if (assignedTester != null) {
            this.assignedTesterId = assignedTester.getId();
        }
    }

    public boolean isReadyToAutomate() {
        return automationStatus != null && "READY_TO_AUTOMATE".equalsIgnoreCase(automationStatus.getCode());
    }

    public boolean isNotAutomatable() {
        return automationStatus != null && "NOT_AUTOMATABLE".equalsIgnoreCase(automationStatus.getCode());
    }

    public boolean isPending() {
        return automationStatus != null && "PENDING".equalsIgnoreCase(automationStatus.getCode());
    }

    // Backward compatibility - String getters
    public String getAutomationStatusCode() {
        return automationStatus != null ? automationStatus.getCode() : null;
    }

    public String getTestCaseTypeCode() {
        return testCaseType != null ? testCaseType.getCode() : null;
    }

    public String getToolTypeCode() {
        return toolType != null ? toolType.getCode() : null;
    }
}