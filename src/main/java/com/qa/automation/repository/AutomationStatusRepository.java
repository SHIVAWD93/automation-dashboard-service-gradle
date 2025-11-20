package com.qa.automation.repository;

import com.qa.automation.model.AutomationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AutomationStatusRepository extends JpaRepository<AutomationStatus, Long> {
    Optional<AutomationStatus> findByCode(String code);
    Optional<AutomationStatus> findByCodeIgnoreCase(String code);
    List<AutomationStatus> findByCodeStatus(String codeStatus);
    List<AutomationStatus> findByCodeStatusOrderBySortBy(String codeStatus);
}