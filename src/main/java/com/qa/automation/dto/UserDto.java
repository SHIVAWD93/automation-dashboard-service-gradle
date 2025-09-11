package com.qa.automation.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String userName;
    private String password;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String token;
    private String permission;
    private Long userPermission;

    public UserDto(String userName, String password, LocalDateTime createdAt, String role, LocalDateTime updatedAt, Long userPermission) {
        this.userName = userName;
        this.password = password;
        this.createdAt = createdAt;
        this.role = role;
        this.updatedAt = updatedAt;
        this.userPermission = userPermission;
    }
}
