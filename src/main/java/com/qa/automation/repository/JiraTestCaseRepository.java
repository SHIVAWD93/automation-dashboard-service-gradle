package com.qa.automation.repository;

import com.qa.automation.model.JiraTestCase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JiraTestCaseRepository extends JpaRepository<JiraTestCase, Long> {

    // Find by sprint ID through Jira issue
    @Query("SELECT jtc FROM JiraTestCase jtc WHERE jtc.jiraIssue.sprintId = :sprintId")
    List<JiraTestCase> findBySprintId(@Param("sprintId") String sprintId);

}
