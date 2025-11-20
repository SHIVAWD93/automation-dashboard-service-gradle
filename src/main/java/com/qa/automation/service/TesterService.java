package com.qa.automation.service;

import com.qa.automation.dto.TesterDto;
import com.qa.automation.model.Role;
import com.qa.automation.model.Tester;
import com.qa.automation.repository.TesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TesterService {

    private final TesterRepository testerRepository;
    private final LookupService lookupService;

    public List<Tester> getAllTesters() {
        List<Tester> testers = testerRepository.findAll();

        testers.forEach(tester -> {
            byte[] imageBytes = tester.getProfileImage();
            if (imageBytes != null && imageBytes.length > 0) {
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                tester.setProfileImageUrl(base64Image);
            } else {
                tester.setProfileImageUrl(null);
            }
        });

        return testers;
    }

    public Tester createTester(Tester tester) {
        // Resolve role lookup
        if (tester.getRole() == null) {
            String roleCode = tester.getRoleCode();
            if (roleCode != null && !roleCode.trim().isEmpty()) {
                tester.setRole(lookupService.findOrCreateRole(roleCode));
            } else {
                // Default to Tester role
                tester.setRole(lookupService.findOrCreateRole("Tester"));
            }
        }

        return testerRepository.save(tester);
    }

    public Tester createTester(TesterDto dto, MultipartFile imageFile) {
        Tester tester = new Tester();
        tester.setName(dto.getName());

        // Resolve role from DTO string
        String roleCode = dto.getRole();
        if (roleCode != null && !roleCode.trim().isEmpty()) {
            tester.setRole(lookupService.findOrCreateRole(roleCode));
        } else {
            tester.setRole(lookupService.findOrCreateRole("Tester"));
        }

        tester.setGender(dto.getGender());
        tester.setExperience(dto.getExperience() != null ? dto.getExperience() : 0);

        Tester savedTester = testerRepository.save(tester);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                savedTester.setProfileImage(imageFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        return testerRepository.save(savedTester);
    }

    public Tester getTesterById(Long id) {
        return testerRepository.findById(id).orElse(null);
    }

    public Tester updateTester(Long id, Tester tester) {
        if (testerRepository.existsById(id)) {
            tester.setId(id);

            // Set default experience if not provided
            if (tester.getExperience() == null) {
                tester.setExperience(0);
            }

            // Resolve role lookup
            if (tester.getRole() == null) {
                String roleCode = tester.getRoleCode();
                if (roleCode != null && !roleCode.trim().isEmpty()) {
                    tester.setRole(lookupService.findOrCreateRole(roleCode));
                }
            }

            return testerRepository.save(tester);
        }
        return null;
    }

    public boolean deleteTester(Long id) {
        if (testerRepository.existsById(id)) {
            testerRepository.deleteById(id);

            // Clean up associated profile image
            Path imagePath = Paths.get("uploads", id + ".png");
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                // Log the failure but don't fail the delete operation
                System.err.println("Failed to delete image for tester ID " + id + ": " + e.getMessage());
            }
            return true;
        }
        return false;
    }

    public Tester findTesterById(Long id) {
        return testerRepository.findById(id).orElse(null);
    }

    /**
     * Helper method for backward compatibility with string role codes
     */
    public Tester createTesterWithRoleCode(String name, String roleCode, String gender, Integer experience) {
        Tester tester = new Tester();
        tester.setName(name);
        tester.setRole(lookupService.findOrCreateRole(roleCode));
        tester.setGender(gender);
        tester.setExperience(experience != null ? experience : 0);
        return testerRepository.save(tester);
    }
}