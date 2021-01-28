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
    FeedbackRepo repo;
    @Autowired
    public FeedbackServiceImpl(FeedbackRepo repo){
        this.repo = repo;
    }

    @Override
    public List<Feedback> getAll(){
        return repo.findAll();
    }
}