package com.qa.automation.repository;

import com.qa.automation.model.Tester;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesterRepository extends JpaRepository<Tester, Long> {

    Optional<Tester> findByName(String name);

}
