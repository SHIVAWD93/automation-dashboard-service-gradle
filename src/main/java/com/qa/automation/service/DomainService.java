package com.qa.automation.service;

import com.qa.automation.exception.ResourceNotFoundException;
import com.qa.automation.model.Domain;
import com.qa.automation.model.WorkflowStatus;
import com.qa.automation.repository.DomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DomainService {

    private final DomainRepository domainRepository;
    private final LookupService lookupService;

    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    public List<Domain> getActiveDomains() {
        // Find domains with Active status
        List<Domain> allDomains = domainRepository.findAll();
        return allDomains.stream()
                .filter(d -> d.getStatus() != null && "Active".equalsIgnoreCase(d.getStatus().getCode()))
                .toList();
    }

    public Domain createDomain(Domain domain) {
        // Check if domain name already exists
        if (domainRepository.existsByName(domain.getName())) {
            throw new ResourceNotFoundException("Domain with name '" + domain.getName() + "' already exists");
        }

        // Resolve status lookup
        if (domain.getStatus() == null) {
            String statusCode = domain.getStatusCode();
            if (statusCode != null && !statusCode.trim().isEmpty()) {
                domain.setStatus(lookupService.findOrCreateWorkflowStatus(statusCode));
            } else {
                // Default to Active
                domain.setStatus(lookupService.findOrCreateWorkflowStatus("Active"));
            }
        }

        return domainRepository.save(domain);
    }

    public Domain getDomainById(Long id) {
        return domainRepository.findById(id).orElse(null);
    }

    public Domain updateDomain(Long id, Domain domain) {
        if (domainRepository.existsById(id)) {
            // Check if new name conflicts with existing domain (excluding current one)
            Optional<Domain> existingDomain = domainRepository.findByName(domain.getName());
            if (existingDomain.isPresent() && !existingDomain.get().getId().equals(id)) {
                throw new RuntimeException("Domain with name '" + domain.getName() + "' already exists");
            }

            domain.setId(id);

            // Resolve status lookup
            if (domain.getStatus() == null) {
                String statusCode = domain.getStatusCode();
                if (statusCode != null && !statusCode.trim().isEmpty()) {
                    domain.setStatus(lookupService.findOrCreateWorkflowStatus(statusCode));
                }
            }

            return domainRepository.save(domain);
        }
        return null;
    }

    public boolean deleteDomain(Long id) {
        if (domainRepository.existsById(id)) {
            domainRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Domain> getDomainsByStatus(String statusCode) {
        List<Domain> allDomains = domainRepository.findAll();
        return allDomains.stream()
                .filter(d -> d.getStatus() != null && statusCode.equalsIgnoreCase(d.getStatus().getCode()))
                .toList();
    }

    /**
     * Helper method for backward compatibility with string status codes
     */
    public Domain createDomainWithStatusCode(String name, String description, String statusCode) {
        Domain domain = new Domain();
        domain.setName(name);
        domain.setDescription(description);
        domain.setStatus(lookupService.findOrCreateWorkflowStatus(statusCode));
        return domainRepository.save(domain);
    }
}