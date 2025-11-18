package com.qa.automation.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TesterDto {

    private String name;
    private String role;
    private String gender;
    private Integer experience;
    private MultipartFile profileImage;

}
