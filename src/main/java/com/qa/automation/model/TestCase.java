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
import lombok.Getter;
import lombok.Setter;

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

    @Column(nullable = false)
    private String priority;

    @Column(nullable = false)
    private String status;

    @Column(name = "test_type")
    private String testType; // "UI" or "API"

    @Column(name = "automation_tool")
    private String automationTool; // "Selenium" or "Tosca"

    // Relationship to Project
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnoreProperties({"testCases", "domain"})
    private Project project;

    // todo: add domain

    // Relationship to Tester
    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tester_id", nullable = false)
    @JsonIgnoreProperties("testCases")
    private Tester tester;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

}
