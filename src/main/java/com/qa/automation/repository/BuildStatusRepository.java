package com.qa.automation.repository;

import com.qa.automation.model.BuildStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface BuildStatusRepository extends JpaRepository<BuildStatus, Long> {
    Optional<BuildStatus> findByCode(String code);
    Optional<BuildStatus> findByCodeIgnoreCase(String code);
    List<BuildStatus> findByCodeStatus(String codeStatus);
    List<BuildStatus> findByCodeStatusOrderBySortBy(String codeStatus);
}