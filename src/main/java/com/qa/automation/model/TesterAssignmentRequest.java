package com.qa.automation.model;

import lombok.Data;

@Data
public class TesterAssignmentRequest {
    private Long automationTesterId;
    private Long manualTesterId;

}
