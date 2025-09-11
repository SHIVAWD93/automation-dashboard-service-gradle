package com.qa.automation.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class JiraIssueDto {
    private Long id;
    private String jiraKey;
    private String summary;
    private String description;
    private String assignee;
    private String assigneeDisplayName;
    private String sprintId;
    private String sprintName;
    private String issueType;
    private String status;
    private String priority;
    private Integer keywordCount;
    private String searchKeyword;
    private List<JiraTestCaseDto> linkedTestCases = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
