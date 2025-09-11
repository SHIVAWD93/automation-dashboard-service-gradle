package com.qa.automation.repository;

import com.qa.automation.model.JenkinsTestCase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JenkinsTestCaseRepository extends JpaRepository<JenkinsTestCase, Long> {

    // Find test cases by Jenkins result ID
    List<JenkinsTestCase> findByJenkinsResultId(Long jenkinsResultId);

}
