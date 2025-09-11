package com.qa.automation.repository;

import com.qa.automation.model.TestCase;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    // Basic finder methods using relationships
    @Query("SELECT tc FROM TestCase tc WHERE tc.project.id = :projectId")
    List<TestCase> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT tc FROM TestCase tc WHERE tc.project.domain.id = :domainId")
    List<TestCase> findByDomainId(@Param("domainId") Long domainId);

    // Count methods
    @Query("SELECT COUNT(tc) FROM TestCase tc WHERE tc.project.id = :projectId")
    long countByProjectId(@Param("projectId") Long projectId);

    long countByStatus(String status);

    @Query("SELECT COUNT(tc) FROM TestCase tc WHERE tc.project.id = :projectId AND tc.status = :status")
    long countByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") String status);

    @Query("SELECT tc.status, COUNT(tc) FROM TestCase tc WHERE tc.project.domain.id = :domainId GROUP BY tc.status")
    List<Object[]> getTestCaseStatsByDomain(@Param("domainId") Long domainId);

}
