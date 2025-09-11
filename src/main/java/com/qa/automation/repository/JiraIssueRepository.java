package com.qa.automation.repository;

import com.qa.automation.model.JiraIssue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JiraIssueRepository extends JpaRepository<JiraIssue, Long> {

    // Find by Jira key (unique identifier)
    Optional<JiraIssue> findByJiraKey(String jiraKey);

    // Find issues by sprint with linked test cases
    @Query("SELECT DISTINCT ji FROM JiraIssue ji LEFT JOIN FETCH ji.linkedTestCases WHERE ji.sprintId = :sprintId")
    List<JiraIssue> findBySprintIdWithLinkedTestCases(@Param("sprintId") String sprintId);

}
