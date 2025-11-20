package com.qa.automation.service;

import com.qa.automation.exception.ResourceNotFoundException;
import com.qa.automation.model.*;
import com.qa.automation.repository.ProjectRepository;
import com.qa.automation.repository.TestCaseRepository;
import com.qa.automation.repository.TesterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final ProjectRepository projectRepository;
    private final TesterRepository testerRepository;
    private final LookupService lookupService;

    // Validation helpers
    private void validateAndAttachManualTester(TestCase testCase) {
        if (testCase.getManualTesterId() != null) {
            Tester manualTester = testerRepository.findById(testCase.getManualTesterId()).orElse(null);
            if (manualTester == null) {
                throw new ResourceNotFoundException("Manual Tester not found with id: " + testCase.getManualTesterId());
            }
            testCase.setManualTester(manualTester);
        } else if (testCase.getManualTester() != null && testCase.getManualTester().getId() != null) {
            Tester manualTester = testerRepository.findById(testCase.getManualTester().getId()).orElse(null);
            if (manualTester == null) {
                throw new ResourceNotFoundException("Manual Tester not found with id: " + testCase.getManualTester().getId());
            }
            testCase.setManualTester(manualTester);
        }
    }

    public List<TestCase> getAllTestCases() {
        return testCaseRepository.findAll();
    }

    public TestCase createTestCase(TestCase testCase) {
        // Handle project assignment
        if (testCase.getProjectId() != null) {
            Project project = projectRepository.findById(testCase.getProjectId()).orElse(null);
            if (project == null) {
                throw new RuntimeException("Project not found with id: " + testCase.getProjectId());
            }
            testCase.setProject(project);
        } else if (testCase.getProject() != null && testCase.getProject().getId() != null) {
            Project project = projectRepository.findById(testCase.getProject().getId()).orElse(null);
            if (project == null) {
                throw new RuntimeException("Project not found with id: " + testCase.getProject().getId());
            }
            testCase.setProject(project);
        } else {
            throw new RuntimeException("Project is required for creating a test case");
        }

        // Handle tester assignment
        if (testCase.getTesterId() != null) {
            Tester tester = testerRepository.findById(testCase.getTesterId()).orElse(null);
            if (tester == null) {
                throw new RuntimeException("Tester not found with id: " + testCase.getTesterId());
            }
            testCase.setTester(tester);
        } else if (testCase.getTester() != null && testCase.getTester().getId() != null) {
            Tester tester = testerRepository.findById(testCase.getTester().getId()).orElse(null);
            if (tester == null) {
                throw new RuntimeException("Tester not found with id: " + testCase.getTester().getId());
            }
            testCase.setTester(tester);
        } else {
            throw new RuntimeException("Tester is required for creating a test case");
        }

        // Handle manual tester assignment (optional)
        validateAndAttachManualTester(testCase);

        // Handle lookup table relationships
        resolveLookupReferences(testCase);

        return testCaseRepository.save(testCase);
    }

    public TestCase getTestCaseById(Long id) {
        return testCaseRepository.findById(id).orElse(null);
    }

    public TestCase updateTestCase(Long id, TestCase testCase) {
        if (testCaseRepository.existsById(id)) {
            testCase.setId(id);
            testCase.setCreatedAt(testCaseRepository.findById(id).get().getCreatedAt());

            // Handle project assignment for update
            if (testCase.getProjectId() != null) {
                Project project = projectRepository.findById(testCase.getProjectId()).orElse(null);
                if (project == null) {
                    throw new RuntimeException("Project not found with id: " + testCase.getProjectId());
                }
                testCase.setProject(project);
            } else if (testCase.getProject() != null && testCase.getProject().getId() != null) {
                Project project = projectRepository.findById(testCase.getProject().getId()).orElse(null);
                if (project == null) {
                    throw new RuntimeException("Project not found with id: " + testCase.getProject().getId());
                }
                testCase.setProject(project);
            }

            // Handle tester assignment for update
            if (testCase.getTesterId() != null) {
                Tester tester = testerRepository.findById(testCase.getTesterId()).orElse(null);
                if (tester == null) {
                    throw new RuntimeException("Tester not found with id: " + testCase.getTesterId());
                }
                testCase.setTester(tester);
            } else if (testCase.getTester() != null && testCase.getTester().getId() != null) {
                Tester tester = testerRepository.findById(testCase.getTester().getId()).orElse(null);
                if (tester == null) {
                    throw new RuntimeException("Tester not found with id: " + testCase.getTester().getId());
                }
                testCase.setTester(tester);
            }

            // Handle manual tester assignment for update (optional)
            validateAndAttachManualTester(testCase);

            // Handle lookup table relationships
            resolveLookupReferences(testCase);

            return testCaseRepository.save(testCase);
        }
        return null;
    }

    public boolean deleteTestCase(Long id) {
        if (testCaseRepository.existsById(id)) {
            testCaseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<TestCase> getTestCasesByProject(Long projectId) {
        return testCaseRepository.findByProjectId(projectId);
    }

    public List<TestCase> getTestCasesByDomain(Long domainId) {
        return testCaseRepository.findByDomainId(domainId);
    }

    /**
     * Resolve lookup table references from code strings
     * This provides backward compatibility for API calls using string codes
     */
    private void resolveLookupReferences(TestCase testCase) {
        // Handle priority - if priority is null but we have a code, look it up
        if (testCase.getPriority() == null) {
            String priorityCode = testCase.getPriorityCode();
            if (priorityCode != null && !priorityCode.trim().isEmpty()) {
                testCase.setPriority(lookupService.findOrCreatePriority(priorityCode));
            } else {
                // Set default priority
                testCase.setPriority(lookupService.findOrCreatePriority("Medium"));
            }
        }

        // Handle status
        if (testCase.getStatus() == null) {
            String statusCode = testCase.getStatusCode();
            if (statusCode != null && !statusCode.trim().isEmpty()) {
                testCase.setStatus(lookupService.findOrCreateWorkflowStatus(statusCode));
            } else {
                // Set default status
                testCase.setStatus(lookupService.findOrCreateWorkflowStatus("In Progress"));
            }
        }

        // Handle test case type (optional)
        if (testCase.getTestCaseType() == null) {
            String typeCode = testCase.getTestCaseTypeCode();
            if (typeCode != null && !typeCode.trim().isEmpty()) {
                testCase.setTestCaseType(lookupService.findOrCreateTestCaseType(typeCode));
            }
        }

        // Handle tool type (optional)
        if (testCase.getToolType() == null) {
            String toolCode = testCase.getToolTypeCode();
            if (toolCode != null && !toolCode.trim().isEmpty()) {
                testCase.setToolType(lookupService.findOrCreateToolType(toolCode));
            }
        }
    }

    /**
     * Helper method for creating test cases with string codes (backward compatibility)
     */
    public TestCase createTestCaseWithCodes(String title, String description,
                                            String priorityCode, String statusCode,
                                            String testCaseTypeCode, String toolTypeCode,
                                            Long projectId, Long testerId, Long manualTesterId) {
        TestCase testCase = new TestCase();
        testCase.setTitle(title);
        testCase.setDescription(description);

        // Set lookup references using codes
        if (priorityCode != null) {
            testCase.setPriority(lookupService.findOrCreatePriority(priorityCode));
        }
        if (statusCode != null) {
            testCase.setStatus(lookupService.findOrCreateWorkflowStatus(statusCode));
        }
        if (testCaseTypeCode != null) {
            testCase.setTestCaseType(lookupService.findOrCreateTestCaseType(testCaseTypeCode));
        }
        if (toolTypeCode != null) {
            testCase.setToolType(lookupService.findOrCreateToolType(toolTypeCode));
        }

        testCase.setProjectId(projectId);
        testCase.setTesterId(testerId);
        testCase.setManualTesterId(manualTesterId);

        return createTestCase(testCase);
    }
}