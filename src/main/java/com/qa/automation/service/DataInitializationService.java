package com.qa.automation.service;

import com.qa.automation.exception.ResourceNotFoundException;
import com.qa.automation.model.Domain;
import com.qa.automation.model.Project;
import com.qa.automation.model.Tester;
import com.qa.automation.repository.DomainRepository;
import com.qa.automation.repository.ProjectRepository;
import com.qa.automation.repository.TesterRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataInitializationService {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationService.class);


    private final DomainRepository domainRepository;


    private final ProjectRepository projectRepository;


    private final TesterRepository testerRepository;

    public void initializeBasicData() {
        logger.info("Initializing basic data to prevent constraint violations...");

        // Ensure default domain exists
        Domain defaultDomain = ensureDefaultDomain();

        // Ensure default project exists
        Project defaultProject = ensureDefaultProject(defaultDomain);

        // Ensure default tester exists
        Tester defaultTester = ensureDefaultTester();

        logger.info("Basic data initialization completed");
    }

    private Domain ensureDefaultDomain() {
        Domain defaultDomain = domainRepository.findByName("Default")
                .orElseGet(() -> {
                    logger.info("Creating default domain");
                    Domain domain = new Domain();
                    domain.setName("Default");
                    domain.setDescription("Default domain for unmapped test cases");
                    domain.setCreatedAt(LocalDateTime.now());
                    domain.setUpdatedAt(LocalDateTime.now());
                    return domainRepository.save(domain);
                });

        logger.debug("Default domain exists with ID: {}", defaultDomain.getId());
        return defaultDomain;
    }

    private Project ensureDefaultProject(Domain defaultDomain) {
        Project defaultProject = projectRepository.findByName("Default Project")
                .orElseGet(() -> {
                    logger.info("Creating default project");
                    Project project = new Project();
                    project.setName("Default Project");
                    project.setDescription("Default project for unmapped test cases");
                    project.setDomain(defaultDomain);
                    project.setCreatedAt(LocalDateTime.now());
                    project.setUpdatedAt(LocalDateTime.now());
                    return projectRepository.save(project);
                });

        logger.debug("Default project exists with ID: {}", defaultProject.getId());
        return defaultProject;
    }

    private Tester ensureDefaultTester() {
        Tester defaultTester = testerRepository.findByName("Unassigned")
                .orElseGet(() -> {
                    logger.info("Creating default tester");
                    Tester tester = new Tester();
                    tester.setName("Unassigned");
                    tester.setCreatedAt(LocalDateTime.now());
                    tester.setUpdatedAt(LocalDateTime.now());
                    return testerRepository.save(tester);
                });

        logger.debug("Default tester exists with ID: {}", defaultTester.getId());
        return defaultTester;
    }

    /**
     * Get default project for fallback scenarios
     */
    public Project getDefaultProject() {
        return projectRepository.findByName("Default Project")
                .orElseThrow(() -> new ResourceNotFoundException("Default project not found"));
    }
    
    /**
     * Get default tester for fallback scenarios
     */
    public Tester getDefaultTester() {
        return testerRepository.findByName("Unassigned")
                .orElseThrow(() -> new ResourceNotFoundException("Default tester not found"));
    }
}
