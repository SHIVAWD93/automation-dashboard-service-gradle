package com.qa.automation.controller;

import com.qa.automation.dto.ApiResponse;
import com.qa.automation.model.Domain;
import com.qa.automation.service.DomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/domains")
@RequiredArgsConstructor
public class DomainController extends BaseController {

    private final DomainService domainService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Domain>>> getAllDomains() {
        logger.info("Fetching all domains");
        List<Domain> domains = domainService.getAllDomains();
        return success(domains, "Domains retrieved successfully");
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Domain>>> getActiveDomains() {
        logger.info("Fetching active domains");
        List<Domain> domains = domainService.getActiveDomains();
        return success(domains, "Active domains retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Domain>> createDomain(@RequestBody Domain domain) {
        logger.info("Creating new domain: {}", domain.getName());
        Domain savedDomain = domainService.createDomain(domain);
        return created(savedDomain, "Domain created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Domain>> getDomainById(@PathVariable Long id) {
        logger.info("Fetching domain by ID: {}", id);
        Domain domain = domainService.getDomainById(id);
        if (domain == null) {
            throwNotFound("Domain", id);
        }
        return success(domain, "Domain retrieved successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Domain>> updateDomain(@PathVariable Long id, @RequestBody Domain domain) {
        logger.info("Updating domain with ID: {}", id);
        Domain updatedDomain = domainService.updateDomain(id, domain);
        if (updatedDomain == null) {
            throwNotFound("Domain", id);
        }
        return success(updatedDomain, "Domain updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(@PathVariable Long id) {
        logger.info("Deleting domain with ID: {}", id);
        boolean deleted = domainService.deleteDomain(id);
        if (!deleted) {
            throwNotFound("Domain", id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Domain>>> getDomainsByStatus(@PathVariable String status) {
        logger.info("Fetching domains by status: {}", status);
        List<Domain> domains = domainService.getDomainsByStatus(status);
        return success(domains, "Domains retrieved successfully by status");
    }
}
