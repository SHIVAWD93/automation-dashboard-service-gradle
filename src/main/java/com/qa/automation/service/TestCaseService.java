package com.qa.automation.service;

import com.qa.automation.exception.ResourceNotFoundException;
import com.qa.automation.model.Project;
import com.qa.automation.model.TestCase;
import com.qa.automation.model.Tester;
import com.qa.automation.repository.ProjectRepository;
import com.qa.automation.repository.TestCaseRepository;
import com.qa.automation.repository.TesterRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestCaseService {


    private final TestCaseRepository testCaseRepository;


    private final ProjectRepository projectRepository;


    private final TesterRepository testerRepository;

    // Validation helpers
    private void validateAndAttachManualTester(com.qa.automation.model.TestCase testCase) {
        if (testCase.getManualTesterId() != null) {
            Tester manualTester = testerRepository.findById(testCase.getManualTesterId()).orElse(null);
            if (manualTester == null) {
                throw new ResourceNotFoundException("Manual Tester not found with id: " + testCase.getManualTesterId());
            }
            testCase.setManualTester(manualTester);
        }
        else if (testCase.getManualTester() != null && testCase.getManualTester().getId() != null) {
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
        }
        else if (testCase.getProject() != null && testCase.getProject().getId() != null) {
            Project project = projectRepository.findById(testCase.getProject().getId()).orElse(null);
            if (project == null) {
                throw new RuntimeException("Project not found with id: " + testCase.getProject().getId());
            }
            testCase.setProject(project);
        }
        else {
            throw new RuntimeException("Project is required for creating a test case");
        }

        // Handle tester assignment
        if (testCase.getTesterId() != null) {
            Tester tester = testerRepository.findById(testCase.getTesterId()).orElse(null);
            if (tester == null) {
                throw new RuntimeException("Tester not found with id: " + testCase.getTesterId());
            }
            testCase.setTester(tester);
        }
        else if (testCase.getTester() != null && testCase.getTester().getId() != null) {
            Tester tester = testerRepository.findById(testCase.getTester().getId()).orElse(null);
            if (tester == null) {
                throw new RuntimeException("Tester not found with id: " + testCase.getTester().getId());
            }
            testCase.setTester(tester);
        }
        else {
            throw new RuntimeException("Tester is required for creating a test case");
        }
        // Handle manual tester assignment (optional)
        validateAndAttachManualTester(testCase);

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
            }
            else if (testCase.getProject() != null && testCase.getProject().getId() != null) {
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
            }
            else if (testCase.getTester() != null && testCase.getTester().getId() != null) {
                Tester tester = testerRepository.findById(testCase.getTester().getId()).orElse(null);
                if (tester == null) {
                    throw new RuntimeException("Tester not found with id: " + testCase.getTester().getId());
                }
                testCase.setTester(tester);
            }

            // Handle manual tester assignment for update (optional)
            validateAndAttachManualTester(testCase);

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

}
