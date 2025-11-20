package com.qa.automation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tool_type_lk")
@Data
public class ToolType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Tool_Type_ID")
    private Long id;

    @Column(name = "Tool_Type_Code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "Tool_Type_Short_Text", length = 100)
    private String shortText;

    @Column(name = "Tool_Type_Long_Text", columnDefinition = "TEXT")
    private String longText;

    @Column(name = "Tool_Type_Code_Status", length = 20)
    private String codeStatus;

    @Column(name = "Tool_Type_Sort_By")
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