package com.feedback.controller;


import com.feedback.repo.entity.Feedback;
import com.feedback.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    FeedbackService feedbackService;
    @Autowired
    public FeedbackController(FeedbackService feedbackService){
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public List<Feedback> getAll(){
        return feedbackService.getAll();
    }
    @PostMapping
    public Feedback saveFeedback(@RequestBody Feedback feedback){
        return feedbackService.saveFeedback(feedback);
    }
}
