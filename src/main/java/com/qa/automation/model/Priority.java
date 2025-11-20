package com.qa.automation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "priority_lk")
@Data
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Priority_ID")
    private Long id;

    @Column(name = "Priority_Code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "Priority_Short_Text", length = 100)
    private String shortText;

    @Column(name = "Priority_Long_Text", columnDefinition = "TEXT")
    private String longText;

    @Column(name = "Priority_Code_Status", length = 20)
    private String codeStatus;

    @Column(name = "Priority_Sort_By")
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