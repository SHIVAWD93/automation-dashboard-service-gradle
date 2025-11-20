package com.qa.automation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestCaseDto {
    private Long id;
    private String title;
    private String description;

    // Use string codes for API compatibility
    private String priority;
    private String status;
    private String testCaseType;
    private String toolType;

    private Long projectId;
    private String projectName;
    private Long testerId;
    private String testerName;
    private Long manualTesterId;
    private String manualTesterName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}