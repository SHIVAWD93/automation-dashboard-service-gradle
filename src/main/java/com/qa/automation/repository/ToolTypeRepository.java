package com.qa.automation.repository;

import com.qa.automation.model.ToolType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ToolTypeRepository extends JpaRepository<ToolType, Long> {
    Optional<ToolType> findByCode(String code);
    Optional<ToolType> findByCodeIgnoreCase(String code);
    List<ToolType> findByCodeStatus(String codeStatus);
    List<ToolType> findByCodeStatusOrderBySortBy(String codeStatus);
}