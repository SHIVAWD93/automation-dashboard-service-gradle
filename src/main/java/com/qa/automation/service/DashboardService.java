package com.qa.automation.service;

import com.qa.automation.model.TestCase;
import com.qa.automation.model.WorkflowStatus;
import com.qa.automation.repository.DomainRepository;
import com.qa.automation.repository.ProjectRepository;
import com.qa.automation.repository.TestCaseRepository;
import com.qa.automation.repository.TesterRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {


    private final DomainRepository domainRepository;

    private final LookupService lookupService;

    private final ProjectRepository projectRepository;


    private final TestCaseRepository testCaseRepository;


    private final TesterRepository testerRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Basic counts (unchanged)
        stats.put("totalDomains", domainRepository.count());
        stats.put("totalProjects", projectRepository.count());
        stats.put("totalTestCases", testCaseRepository.count());
        stats.put("totalTesters", testerRepository.count());

        // NEW: Use lookup service to get status IDs for counting
        WorkflowStatus activeStatus = lookupService.findOrCreateWorkflowStatus("Active");

        // Count active domains
        long activeDomains = domainRepository.findAll().stream()
                .filter(d -> d.getStatus() != null &&
                        d.getStatus().getId().equals(activeStatus.getId()))
                .count();
        stats.put("activeDomains", activeDomains);

        // Count active projects
        long activeProjects = projectRepository.findAll().stream()
                .filter(p -> p.getStatus() != null &&
                        p.getStatus().getId().equals(activeStatus.getId()))
                .count();
        stats.put("activeProjects", activeProjects);

        // NEW: Test case counts using workflow statuses
        WorkflowStatus automatedStatus = lookupService.findOrCreateWorkflowStatus("Automated");
        WorkflowStatus inProgressStatus = lookupService.findOrCreateWorkflowStatus("In Progress");
        WorkflowStatus readyStatus = lookupService.findOrCreateWorkflowStatus("Ready to Automate");
        WorkflowStatus completedStatus = lookupService.findOrCreateWorkflowStatus("Completed");
        WorkflowStatus cannotStatus = lookupService.findOrCreateWorkflowStatus("Cannot be Automated");

        List<TestCase> allTestCases = testCaseRepository.findAll();

        stats.put("automatedTestCases", allTestCases.stream()
                .filter(tc -> tc.getStatus() != null &&
                        tc.getStatus().getId().equals(automatedStatus.getId()))
                .count());
        stats.put("inProgressTestCases", allTestCases.stream()
                .filter(tc -> tc.getStatus() != null &&
                        tc.getStatus().getId().equals(inProgressStatus.getId()))
                .count());
        stats.put("readyTestCases", allTestCases.stream()
                .filter(tc -> tc.getStatus() != null &&
                        tc.getStatus().getId().equals(readyStatus.getId()))
                .count());
        stats.put("completedTestCases", allTestCases.stream()
                .filter(tc -> tc.getStatus() != null &&
                        tc.getStatus().getId().equals(completedStatus.getId()))
                .count());
        stats.put("cannotBeAutomated", allTestCases.stream()
                .filter(tc -> tc.getStatus() != null &&
                        tc.getStatus().getId().equals(cannotStatus.getId()))
                .count());

        return stats;
    }

    public Map<String, Object> getDomainStats(Long domainId) {
        Map<String, Object> stats = new HashMap<>();

        // Projects in domain
        stats.put("totalProjects", projectRepository.countByDomainId(domainId));
        stats.put("activeProjects", projectRepository.countByDomainIdAndStatus(domainId, "Active"));

        // Test cases in domain
        List<Object[]> testCaseStats = testCaseRepository.getTestCaseStatsByDomain(domainId);
        processTestCaseStats(stats, testCaseStats);

        return stats;
    }

    public Map<String, Object> getProjectStats(Long projectId) {
        Map<String, Object> stats = new HashMap<>();

        // Test cases in project
        stats.put("totalTestCases", testCaseRepository.countByProjectId(projectId));
        stats.put("automatedTestCases", testCaseRepository.countByProjectIdAndStatus(projectId, "Automated"));
        stats.put("inProgressTestCases", testCaseRepository.countByProjectIdAndStatus(projectId, "In Progress"));
        stats.put("readyTestCases", testCaseRepository.countByProjectIdAndStatus(projectId, "Ready to Automate"));
        stats.put("completedTestCases", testCaseRepository.countByProjectIdAndStatus(projectId, "Completed"));

        return stats;
    }

    private void processTestCaseStats(Map<String, Object> stats, List<Object[]> testCaseStats) {
        long totalTestCases = 0;
        long automatedTestCases = 0;
        long inProgressTestCases = 0;
        long readyTestCases = 0;
        long completedTestCases = 0;

        for (Object[] stat : testCaseStats) {
            String status = (String) stat[0];
            Long count = (Long) stat[1];

            totalTestCases += count;

            switch (status) {
                case "Automated":
                    automatedTestCases = count;
                    break;
                case "In Progress":
                    inProgressTestCases = count;
                    break;
                case "Ready to Automate":
                    readyTestCases = count;
                    break;
                case "Completed":
                    completedTestCases = count;
                    break;
                default:
                    break;
            }
        }

        stats.put("totalTestCases", totalTestCases);
        stats.put("automatedTestCases", automatedTestCases);
        stats.put("inProgressTestCases", inProgressTestCases);
        stats.put("readyTestCases", readyTestCases);
        stats.put("completedTestCases", completedTestCases);
    }
}
