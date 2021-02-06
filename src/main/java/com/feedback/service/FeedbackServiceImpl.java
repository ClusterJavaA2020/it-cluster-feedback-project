package com.feedback.service;

import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.entity.Feedback;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Builder
@Data
@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepo repo;
    @Autowired
    public FeedbackServiceImpl(FeedbackRepo repo){
        this.repo = repo;
    }

    @Override
    public List<Feedback> getAllByFeedbackRequestId(int id){
        return repo.findAllByFeedbackRequestId(id);
    }
}