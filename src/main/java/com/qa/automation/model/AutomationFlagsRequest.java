package com.qa.automation.model;

import lombok.Data;

@Data
public class AutomationFlagsRequest {
    private boolean canBeAutomated;
    private boolean cannotBeAutomated;
}
