package com.qa.automation.model;

import lombok.Data;

@Data
public class CombinedSaveRequest {
    private String notes;
    private Long automationTesterId;
    private Long manualTesterId;
    private Long projectId; // NEW: Added project support
}
