package com.qa.automation.model;

import lombok.Data;

@Data
public class SaveTestCaseRequest {
    private Long projectId;
    private Long testerId;
    private boolean canBeAutomated;
    private boolean cannotBeAutomated;
    private Long manualTesterId;
    private String testCaseType;
    private String toolType;

}
