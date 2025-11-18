package com.qa.automation.repository;

import com.qa.automation.model.Feedback;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByTesterId(Long testerId);
}
