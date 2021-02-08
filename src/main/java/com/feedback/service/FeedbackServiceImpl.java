package com.feedback.service;

import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.entity.Feedback;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepo repo;
    public FeedbackServiceImpl(FeedbackRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Feedback> getAllByFeedbackRequestId(int id) {
        return repo.findAllByFeedbackRequestId(id);
    }
}