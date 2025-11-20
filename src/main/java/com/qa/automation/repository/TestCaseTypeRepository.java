package com.qa.automation.repository;

import com.qa.automation.model.TestCaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface TestCaseTypeRepository extends JpaRepository<TestCaseType, Long> {
    Optional<TestCaseType> findByCode(String code);
    Optional<TestCaseType> findByCodeIgnoreCase(String code);
    List<TestCaseType> findByCodeStatus(String codeStatus);
    List<TestCaseType> findByCodeStatusOrderBySortBy(String codeStatus);
}