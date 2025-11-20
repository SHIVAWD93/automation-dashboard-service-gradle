package com.qa.automation.repository;

import com.qa.automation.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
    Optional<Priority> findByCode(String code);
    Optional<Priority> findByCodeIgnoreCase(String code);
    List<Priority> findByCodeStatus(String codeStatus);
    List<Priority> findByCodeStatusOrderBySortBy(String codeStatus);
}