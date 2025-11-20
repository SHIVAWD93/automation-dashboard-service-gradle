package com.qa.automation.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "testers")
@Data
public class Tester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Updated to use Role lookup table
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private String gender;

    @Column(name = "experience", nullable = false)
    private Integer experience = 0;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Tester() {}

    public Tester(String name, Role role, String gender, Integer experience) {
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.experience = experience;
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

    // Backward compatibility - String getter/setter
    public String getRoleCode() {
        return role != null ? role.getCode() : null;
    }
}