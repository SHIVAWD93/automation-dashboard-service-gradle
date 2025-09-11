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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "jenkins_test_cases")
@Data
public class JenkinsTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String testName;

    @Column(name = "class_name")
    private String className;

    @Column(nullable = false)
    private String status; // PASSED, FAILED, SKIPPED

    @Column(name = "duration")
    private Double duration; // Test execution duration in seconds

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jenkins_result_id", nullable = false)
    @JsonIgnoreProperties("testCases")
    private JenkinsResult jenkinsResult;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
