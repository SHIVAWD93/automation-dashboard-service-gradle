package com.qa.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jenkins_results")
@Data
public class JenkinsResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_name", nullable = false)
    private String jobName;

    @Column(name = "build_number", nullable = false)
    private String buildNumber;

    // Updated to use BuildStatus lookup table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "build_status_id")
    private BuildStatus buildStatus;

    @Column(name = "build_url")
    private String buildUrl;

    @Column(name = "build_timestamp")
    private LocalDateTime buildTimestamp;

    @Column(name = "total_tests")
    private Integer totalTests;

    @Column(name = "passed_tests")
    private Integer passedTests;

    @Column(name = "failed_tests")
    private Integer failedTests;

    @Column(name = "skipped_tests")
    private Integer skippedTests;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "pass_percentage")
    private Integer passPercentage;

    @Column(name = "bugs_identified", columnDefinition = "TEXT")
    private String bugsIdentified;

    @Column(name = "failure_reasons", columnDefinition = "TEXT")
    private String failureReasons;

    @Column(name = "job_frequency")
    private String jobFrequency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "automation_tester_id")
    private Tester automationTester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manual_tester_id")
    private Tester manualTester;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "jenkinsResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("jenkinsResult")
    private List<JenkinsTestCase> testCases;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (jobFrequency == null) {
            jobFrequency = "Unknown";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void inferJobFrequency() {
        if (jobName == null) {
            this.jobFrequency = "Unknown";
            return;
        }

        String lowerJobName = jobName.toLowerCase();
        if (lowerJobName.contains("hourly")) {
            this.jobFrequency = "Hourly";
        } else if (lowerJobName.contains("daily") || lowerJobName.contains("nightly")) {
            this.jobFrequency = "Daily";
        } else if (lowerJobName.contains("weekly")) {
            this.jobFrequency = "Weekly";
        } else if (lowerJobName.contains("monthly")) {
            this.jobFrequency = "Monthly";
        } else if (lowerJobName.contains("manual") || lowerJobName.contains("ondemand") || lowerJobName.contains("trigger")) {
            this.jobFrequency = "On Demand";
        } else if (lowerJobName.contains("continuous") || lowerJobName.contains("ci") || lowerJobName.contains("commit")) {
            this.jobFrequency = "Continuous";
        } else {
            this.jobFrequency = "Unknown";
        }
    }

    // Backward compatibility - String getter/setter
    public String getBuildStatusCode() {
        return buildStatus != null ? buildStatus.getCode() : null;
    }
}
