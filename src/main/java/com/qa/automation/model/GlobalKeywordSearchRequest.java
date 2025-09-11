package com.qa.automation.model;

import lombok.Data;

@Data
public class GlobalKeywordSearchRequest {
    private String keyword;
    private String jiraProjectKey;
    private String sprintId;
}
