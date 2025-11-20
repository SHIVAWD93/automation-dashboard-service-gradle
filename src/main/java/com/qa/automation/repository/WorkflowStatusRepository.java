package com.qa.automation.repository;

import com.qa.automation.model.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface WorkflowStatusRepository extends JpaRepository<WorkflowStatus, Long> {
    Optional<WorkflowStatus> findByCode(String code);
    Optional<WorkflowStatus> findByCodeIgnoreCase(String code);
    List<WorkflowStatus> findByCodeStatus(String codeStatus);
    List<WorkflowStatus> findByCodeStatusOrderBySortBy(String codeStatus);
}