package com.qa.automation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_case_type_lk")
@Data
public class TestCaseType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Test_Case_Type_ID")
    private Long id;

    @Column(name = "Test_Case_Type_Code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "Test_Case_Type_Short_Text", length = 100)
    private String shortText;

    @Column(name = "Test_Case_Type_Long_Text", columnDefinition = "TEXT")
    private String longText;

    @Column(name = "Test_Case_Type_Code_Status", length = 20)
    private String codeStatus;

    @Column(name = "Test_Case_Type_Sort_By")
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