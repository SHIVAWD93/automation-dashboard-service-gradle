package com.qa.automation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DomainDto {
    private Long id;
    private String name;
    private String description;

    // Use string code for API compatibility
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}