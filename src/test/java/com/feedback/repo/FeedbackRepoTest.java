package com.feedback.repo;

import com.feedback.repo.entity.Feedback;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FeedbackRepoTest {

    @Autowired
    FeedbackRepo feedbackRepo;

    @Test
    void testFindById() {
        Feedback feedback = feedback();
        feedbackRepo.save(feedback);
        Optional<Feedback> result = feedbackRepo.findById(feedback.getId());
        assertTrue(result.isPresent());
        assertEquals(feedback, result.get());
    }

    @Test
    void testFindByFeedbackRequestId() {
        Feedback feedback = feedback();
        feedbackRepo.save(feedback);
        List<Feedback> result = feedbackRepo.findByFeedbackRequestId(2L);
        assertFalse(result.isEmpty());
        assertEquals(feedback, result.get(0));
    }

    private Feedback feedback() {
        return Feedback.builder()
                .answer(new HashSet<>())
                .feedbackRequestId(2L)
                .userId(3L)
                .isClosed(false)
                .build();
    }
}
