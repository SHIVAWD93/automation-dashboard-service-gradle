package com.qa.automation.controller;

import com.qa.automation.model.Project;
import com.qa.automation.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            log.info("Fetching all projects");
            List<Project> projects = projectService.getAllProjects();
            log.info("Retrieved {} projects", projects.size());
            return ResponseEntity.ok(projects);
        }
        catch (Exception e) {
            log.error("Error fetching all projects: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        try {
            log.info("Creating new project: {}", project.getName());
            Project savedProject = projectService.createProject(project);
            log.info("Successfully created project with ID: {}", savedProject.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
        }
        catch (RuntimeException e) {
            log.warn("Failed to create project: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            log.error("Error creating project: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        try {
            log.info("Fetching project by ID: {}", id);
            Project project = projectService.getProjectById(id);
            if (project != null) {
                return ResponseEntity.ok(project);
            }
            log.warn("Project not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            log.error("Error fetching project by ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody Project project) {
        try {
            log.info("Updating project with ID: {}", id);
            Project updatedProject = projectService.updateProject(id, project);
            if (updatedProject != null) {
                log.info("Successfully updated project with ID: {}", id);
                return ResponseEntity.ok(updatedProject);
            }
            log.warn("Project not found for update with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        catch (RuntimeException e) {
            log.warn("Failed to update project with ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            log.error("Error updating project with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            log.info("Deleting project with ID: {}", id);
            boolean deleted = projectService.deleteProject(id);
            if (deleted) {
                log.info("Successfully deleted project with ID: {}", id);
                return ResponseEntity.noContent().build();
            }
            log.warn("Project not found for deletion with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            log.error("Error deleting project with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/domain/{domainId}")
    public ResponseEntity<List<Project>> getProjectsByDomain(@PathVariable Long domainId) {
        try {
            log.info("Fetching projects by domain ID: {}", domainId);
            List<Project> projects = projectService.getProjectsByDomain(domainId);
            log.info("Retrieved {} projects for domain ID: {}", projects.size(), domainId);
            return ResponseEntity.ok(projects);
        }
        catch (Exception e) {
            log.error("Error fetching projects by domain ID {}: {}", domainId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/domain/{domainId}/status/{status}")
    public ResponseEntity<List<Project>> getProjectsByDomainAndStatus(
            @PathVariable Long domainId, @PathVariable String status) {
        try {
            log.info("Fetching projects by domain ID: {} and status: {}", domainId, status);
            List<Project> projects = projectService.getProjectsByDomainAndStatus(domainId, status);
            log.info("Retrieved {} projects for domain ID: {} and status: {}", projects.size(), domainId, status);
            return ResponseEntity.ok(projects);
        }
        catch (Exception e) {
            log.error("Error fetching projects by domain ID {} and status {}: {}", domainId, status, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Project>> getProjectsByStatus(@PathVariable String status) {
        try {
            log.info("Fetching projects by status: {}", status);
            List<Project> projects = projectService.getProjectsByStatus(status);
            log.info("Retrieved {} projects for status: {}", projects.size(), status);
            return ResponseEntity.ok(projects);
        }
        catch (Exception e) {
            log.error("Error fetching projects by status {}: {}", status, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
