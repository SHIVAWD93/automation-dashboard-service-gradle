package com.qa.automation.model;

import lombok.Data;

@Data
public class SaveTestCaseRequest {
    private Long projectId;
    private Long testerId;
    private boolean canBeAutomated;
    private boolean cannotBeAutomated;
    private String testType; // "UI" or "API"
    private String automationTool; // "Selenium" or "Tosca"

}
