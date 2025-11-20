package com.qa.automation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "automation_status_lk")
@Data
public class AutomationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Automation_Status_ID")
    private Long id;

    @Column(name = "Automation_Status_Code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "Automation_Status_Short_Text", length = 100)
    private String shortText;

    @Column(name = "Automation_Status_Long_Text", columnDefinition = "TEXT")
    private String longText;

    @Column(name = "Automation_Status_Code_Status", length = 20)
    private String codeStatus;

    @Column(name = "Automation_Status_Sort_By")
    private Integer sortBy;

    @Column(name = "Created_At")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (codeStatus == null) {
            codeStatus = "Active";
        }
    }
}