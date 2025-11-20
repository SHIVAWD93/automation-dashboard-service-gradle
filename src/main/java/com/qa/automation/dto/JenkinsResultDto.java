package com.qa.automation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JenkinsResultDto {
    private Long id;
    private String jobName;
    private String buildNumber;

    // Use string code for API compatibility
    private String buildStatus;

    private String buildUrl;
    private LocalDateTime buildTimestamp;
    private Integer totalTests;
    private Integer passedTests;
    private Integer failedTests;
    private Integer skippedTests;
    private Integer passPercentage;
    private String bugsIdentified;
    private String failureReasons;
    private String jobFrequency;

    private Long automationTesterId;
    private String automationTesterName;
    private Long manualTesterId;
    private String manualTesterName;
    private Long projectId;
    private String projectName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}