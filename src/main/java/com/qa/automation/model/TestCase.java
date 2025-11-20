package com.qa.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_cases")
@Data
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // Updated to use Priority lookup table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "priority_id")
    private Priority priority;

    // Updated to use WorkflowStatus lookup table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private WorkflowStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnoreProperties({"testCases", "domain"})
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tester_id", nullable = false)
    @JsonIgnoreProperties("testCases")
    private Tester tester;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Helper methods for backward compatibility
    public Long getProjectId() {
        return project != null ? project.getId() : null;
    }

    public void setProjectId(Long projectId) {
        if (projectId != null) {
            if (this.project == null) {
                this.project = new Project();
            }
            this.project.setId(projectId);
        }
    }

    public Long getTesterId() {
        return tester != null ? tester.getId() : null;
    }

    public void setTesterId(Long testerId) {
        if (testerId != null) {
            if (this.tester == null) {
                this.tester = new Tester();
            }
            this.tester.setId(testerId);
        }
    }
    public Long getManualTesterId() {
        return manualTester != null ? manualTester.getId() : null;
    }

    public void setManualTesterId(Long manualTesterId) {
        if (manualTesterId != null) {
            if (this.manualTester == null) {
                this.manualTester = new Tester();
            }
            this.manualTester.setId(manualTesterId);
        }
    }

    public String getPriorityCode() {
        return priority != null ? priority.getCode() : null;
    }

    public String getStatusCode() {
        return status != null ? status.getCode() : null;
    }

    public String getTestCaseTypeCode() {
        return testCaseType != null ? testCaseType.getCode() : null;
    }

    public String getToolTypeCode() {
        return toolType != null ? toolType.getCode() : null;
    }
}
