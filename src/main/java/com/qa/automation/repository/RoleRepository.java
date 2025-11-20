package com.qa.automation.repository;

import com.qa.automation.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(String code);
    Optional<Role> findByCodeIgnoreCase(String code);
    List<Role> findByCodeStatus(String codeStatus);
    List<Role> findByCodeStatusOrderBySortBy(String codeStatus);
}