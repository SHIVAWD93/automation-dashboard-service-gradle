package com.qa.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    // Updated to use WorkflowStatus lookup table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private WorkflowStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_id", nullable = false)
    @JsonIgnoreProperties("projects")
    private Domain domain;

    @Column(name = "jira_project_key")
    private String jiraProjectKey;

    @Column(name = "jira_board_id")
    private String jiraBoardId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Project() {}

    public Project(String name, String description, WorkflowStatus status, Domain domain) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.domain = domain;
    }

    public Project(String name, String description, WorkflowStatus status, Domain domain,
                   String jiraProjectKey, String jiraBoardId) {
        this(name, description, status, domain);
        this.jiraProjectKey = jiraProjectKey;
        this.jiraBoardId = jiraBoardId;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Backward compatibility - String getter/setter
    public String getStatusCode() {
        return status != null ? status.getCode() : null;
    }
}
