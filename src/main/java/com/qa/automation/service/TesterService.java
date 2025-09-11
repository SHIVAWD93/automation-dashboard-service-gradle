package com.qa.automation.service;

import com.qa.automation.model.Tester;
import com.qa.automation.repository.TesterRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TesterService {

    private final TesterRepository testerRepository;

    public List<Tester> getAllTesters() {
        List<Tester> testers = testerRepository.findAll();

        testers.forEach(tester -> {
            Path imagePath = Paths.get("uploads", tester.getId() + ".png");
            try {
                byte[] imageBytes = Files.readAllBytes(imagePath);
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                tester.setProfileImageUrl(base64Image);  // base64 string instead of URL
            }
            catch (IOException e) {
                // If file not found or error, you can either set null or some default
                tester.setProfileImageUrl(null);
                // optionally log error here
            }
        });

        return testers;
    }


    public Tester createTester(Tester tester) {
        return testerRepository.save(tester);
    }

    public Tester createTester(Tester tester, MultipartFile imageFile) {
        if (tester.getExperience() == null) {
            tester.setExperience(0);
        }
        Tester savedTester = testerRepository.save(tester);
        if (!imageFile.isEmpty()) {
            // Prepare file path
            String uploadDir = "uploads/";
            String fileName = savedTester.getId() + ".png";
            File file = new File(uploadDir + fileName);
            file.getParentFile().mkdirs(); // ensure directory exists
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(imageFile.getBytes());
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
            // Save image path to DB
            savedTester.setProfileImageUrl(uploadDir + fileName);
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
            }
            catch (IOException e) {
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
}
