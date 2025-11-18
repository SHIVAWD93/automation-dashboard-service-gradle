package com.qa.automation.controller;

import com.qa.automation.dto.FeedbackDto;
import com.qa.automation.model.Feedback;
import com.qa.automation.service.FeedbackService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission('automation-dashboard.admin')")
    public ResponseEntity<Feedback> createFeedback(@RequestBody FeedbackDto feedback) {
        return ResponseEntity.ok(feedbackService.saveFeedback(feedback));
    }


    @GetMapping("{testerId}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission('automation-dashboard.admin')")
    public ResponseEntity<List<Feedback>> getFeedbackByTesterId(@PathVariable Long testerId) {
        List<Feedback> feedbackList = feedbackService.getFeedbackByTesterId(testerId);
        return ResponseEntity.ok(feedbackList);
    }


    @GetMapping
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission('automation-dashboard.admin')")
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedback());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "@amsHelper.hasGlobalPermission('automation-dashboard.admin')")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

}
