package com.qa.automation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

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

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_id", nullable = false)
    @JsonIgnoreProperties("projects")
    private Domain domain;

    // NEW: Jira configuration fields
    @Column(name = "jira_project_key")
    private String jiraProjectKey;

    @Column(name = "jira_board_id")
    private String jiraBoardId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Project() {
    }

    public Project(String name, String description, String status, Domain domain) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.domain = domain;
    }

    public Project(String name, String description, String status, Domain domain,
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

}
