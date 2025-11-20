package com.qa.automation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "role_lk")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Role_ID")
    private Long id;

    @Column(name = "Role_Code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "Role_Short_Text", length = 100)
    private String shortText;

    @Column(name = "Role_Long_Text", columnDefinition = "TEXT")
    private String longText;

    @Column(name = "Role_Code_Status", length = 20)
    private String codeStatus;

    @Column(name = "Role_Sort_By")
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