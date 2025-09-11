package com.qa.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

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

    @Column(name = "automation_status")
    private String automationStatus; // "READY_TO_AUTOMATE", "NOT_AUTOMATABLE", "PENDING"

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

        // Set automation status based on checkbox values
        updateAutomationStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateAutomationStatus();
    }

    // Private method to update automation status
    private void updateAutomationStatus() {
        if (canBeAutomated && !cannotBeAutomated) {
            this.automationStatus = "Ready to Automate";
        }
        else if (!canBeAutomated && cannotBeAutomated) {
            this.automationStatus = "NOT_AUTOMATABLE";
        }
        else {
            this.automationStatus = "PENDING";
        }
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

    // Helper methods
    public boolean isReadyToAutomate() {
        return "Ready to Automate".equals(automationStatus);
    }

    public boolean isNotAutomatable() {
        return "NOT_AUTOMATABLE".equals(automationStatus);
    }

    public boolean isPending() {
        return "PENDING".equals(automationStatus);
    }

}
