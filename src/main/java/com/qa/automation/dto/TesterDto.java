package com.qa.automation.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TesterDto {
    private Long id;
    private String name;

    // Use string code for API compatibility
    private String role;

    private String gender;
    private Integer experience;
    private MultipartFile profileImage;
    private String profileImageUrl;
}