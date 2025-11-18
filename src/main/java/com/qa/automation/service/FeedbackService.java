package com.qa.automation.service;

import com.qa.automation.dto.FeedbackDto;
import com.qa.automation.model.Feedback;
import com.qa.automation.model.Tester;
import com.qa.automation.repository.FeedbackRepository;
import com.qa.automation.repository.TesterRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final TesterRepository testerRepository;

    public Feedback saveFeedback(FeedbackDto dto) {
        Tester tester = testerRepository.findById(dto.getTesterId())
                .orElseThrow(() -> new RuntimeException("Tester not found"));

        Feedback feedback = new Feedback();
        feedback.setFeedback(dto.getFeedback());
        feedback.setTester(tester);

        return feedbackRepository.save(feedback);
    }


    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    public List<Feedback> getFeedbackByTesterId(Long testerId) {
        return feedbackRepository.findByTesterId(testerId);
    }

    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback not found with ID: " + id);
        }
        feedbackRepository.deleteById(id);
    }


}
