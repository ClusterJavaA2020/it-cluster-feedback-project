package com.feedback.service;

import com.feedback.repo.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    List<Feedback> getAll();
    Feedback saveFeedback(Feedback feedback);

}
