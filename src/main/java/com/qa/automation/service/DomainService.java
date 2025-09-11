package com.qa.automation.service;

import com.qa.automation.exception.ResourceNotFoundException;
import com.qa.automation.model.Domain;
import com.qa.automation.repository.DomainRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainService {

    private final DomainRepository domainRepository;

    public List<Domain> getAllDomains() {
        return domainRepository.findAll();
    }

    public List<Domain> getActiveDomains() {
        return domainRepository.findActiveDomains();
    }

    public Domain createDomain(Domain domain) {
        // Check if domain name already exists
        if (domainRepository.existsByName(domain.getName())) {
            throw new ResourceNotFoundException("Domain with name '" + domain.getName() + "' already exists");
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

    public List<Domain> getDomainsByStatus(String status) {
        return domainRepository.findByStatus(status);
    }

}
