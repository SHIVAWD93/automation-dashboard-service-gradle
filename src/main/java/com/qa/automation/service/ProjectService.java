package com.qa.automation.service;

import com.qa.automation.model.Domain;
import com.qa.automation.model.Project;
import com.qa.automation.model.WorkflowStatus;
import com.qa.automation.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final DomainService domainService;
    private final LookupService lookupService;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        // Validate that domain exists
        if (project.getDomain() == null || project.getDomain().getId() == null) {
            throw new RuntimeException("Domain is required for creating a project");
        }

        Domain domain = domainService.getDomainById(project.getDomain().getId());
        if (domain == null) {
            throw new RuntimeException("Domain not found with id: " + project.getDomain().getId());
        }

        project.setDomain(domain);

        // Resolve status lookup
        if (project.getStatus() == null) {
            String statusCode = project.getStatusCode();
            if (statusCode != null && !statusCode.trim().isEmpty()) {
                project.setStatus(lookupService.findOrCreateWorkflowStatus(statusCode));
            } else {
                // Default to Active
                project.setStatus(lookupService.findOrCreateWorkflowStatus("Active"));
            }
        }

        return projectRepository.save(project);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project updateProject(Long id, Project project) {
        if (projectRepository.existsById(id)) {
            // Validate domain if provided
            if (project.getDomain() != null && project.getDomain().getId() != null) {
                Domain domain = domainService.getDomainById(project.getDomain().getId());
                if (domain == null) {
                    throw new RuntimeException("Domain not found with id: " + project.getDomain().getId());
                }
                project.setDomain(domain);
            }

            project.setId(id);

            // Resolve status lookup
            if (project.getStatus() == null) {
                String statusCode = project.getStatusCode();
                if (statusCode != null && !statusCode.trim().isEmpty()) {
                    project.setStatus(lookupService.findOrCreateWorkflowStatus(statusCode));
                }
            }

            return projectRepository.save(project);
        }
        return null;
    }

    public boolean deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Project> getProjectsByDomain(Long domainId) {
        return projectRepository.findByDomainId(domainId);
    }

    public List<Project> getProjectsByDomainAndStatus(Long domainId, String statusCode) {
        List<Project> projects = projectRepository.findByDomainId(domainId);
        return projects.stream()
                .filter(p -> p.getStatus() != null && statusCode.equalsIgnoreCase(p.getStatus().getCode()))
                .toList();
    }

    public List<Project> getProjectsByStatus(String statusCode) {
        List<Project> allProjects = projectRepository.findAll();
        return allProjects.stream()
                .filter(p -> p.getStatus() != null && statusCode.equalsIgnoreCase(p.getStatus().getCode()))
                .toList();
    }

    public Project findProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    /**
     * Helper method for backward compatibility with string status codes
     */
    public Project createProjectWithStatusCode(String name, String description,
                                               String statusCode, Long domainId) {
        Domain domain = domainService.getDomainById(domainId);
        if (domain == null) {
            throw new RuntimeException("Domain not found with id: " + domainId);
        }

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setDomain(domain);
        project.setStatus(lookupService.findOrCreateWorkflowStatus(statusCode));
        return projectRepository.save(project);
    }
}