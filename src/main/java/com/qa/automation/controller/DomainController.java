package com.qa.automation.controller;

import com.qa.automation.model.Domain;
import com.qa.automation.service.DomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/domains")
@RequiredArgsConstructor
@Slf4j
public class DomainController {

    private final DomainService domainService;

    @GetMapping
    public ResponseEntity<List<Domain>> getAllDomains() {
        log.info("Fetching all domains");
        List<Domain> domains = domainService.getAllDomains();
        return ResponseEntity.ok(domains);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Domain>> getActiveDomains() {
        log.info("Fetching active domains");
        List<Domain> domains = domainService.getActiveDomains();
        return ResponseEntity.ok(domains);
    }

    @PostMapping
    public ResponseEntity<?> createDomain(@RequestBody Domain domain) {
        log.info("Creating new domain: {}", domain.getName());
        Domain savedDomain = domainService.createDomain(domain);
        log.info("Successfully created domain with ID: {}", savedDomain.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDomain);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Domain> getDomainById(@PathVariable Long id) {
        log.info("Fetching domain by ID: {}", id);
        Domain domain = domainService.getDomainById(id);
        if (domain != null) {
            return ResponseEntity.ok(domain);
        }
        log.warn("Domain not found with ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDomain(@PathVariable Long id, @RequestBody Domain domain) {
        log.info("Updating domain with ID: {}", id);
        Domain updatedDomain = domainService.updateDomain(id, domain);
        if (updatedDomain != null) {
            log.info("Successfully updated domain with ID: {}", id);
            return ResponseEntity.ok(updatedDomain);
        }
        log.warn("Domain not found for update with ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(@PathVariable Long id) {
        log.info("Deleting domain with ID: {}", id);
        boolean deleted = domainService.deleteDomain(id);
        if (deleted) {
            log.info("Successfully deleted domain with ID: {}", id);
            return ResponseEntity.noContent().build();
        }
        log.warn("Domain not found for deletion with ID: {}", id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Domain>> getDomainsByStatus(@PathVariable String status) {
        log.info("Fetching domains by status: {}", status);
        List<Domain> domains = domainService.getDomainsByStatus(status);
        return ResponseEntity.ok(domains);
    }
}
