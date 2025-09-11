package com.qa.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "jira_issues")
@Data
public class JiraIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jira_key", nullable = false, unique = true)
    private String jiraKey;

    @Column(nullable = false)
    private String summary;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "assignee_display_name")
    private String assigneeDisplayName;

    @Column(name = "sprint_id")
    private String sprintId;

    @Column(name = "sprint_name")
    private String sprintName;

    @Column(name = "issue_type")
    private String issueType;

    @Column(name = "status")
    private String status;

    @Column(name = "priority")
    private String priority;

    @Column(name = "keyword_count")
    private Integer keywordCount = 0;

    @Column(name = "search_keyword")
    private String searchKeyword;

    @OneToMany(mappedBy = "jiraIssue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("jiraIssue")
    private List<JiraTestCase> linkedTestCases = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    // Helper methods
    public void addLinkedTestCase(JiraTestCase testCase) {
        linkedTestCases.add(testCase);
        testCase.setJiraIssue(this);
    }

    public void removeLinkedTestCase(JiraTestCase testCase) {
        linkedTestCases.remove(testCase);
        testCase.setJiraIssue(null);
    }

}
