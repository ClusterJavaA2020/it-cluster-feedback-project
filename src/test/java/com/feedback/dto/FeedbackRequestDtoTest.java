package com.feedback.dto;

import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.FeedbackRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FeedbackRequestDtoTest {

    @Test
    void testMap() {
        FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                .id(1L)
                .active(true)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .course(Course.builder()
                        .title("Test course")
                        .build())
                .build();
        FeedbackRequestDto feedbackRequestDto = FeedbackRequestDto.map(feedbackRequest);
        assertEquals(feedbackRequest.getId(), feedbackRequestDto.getId());
        assertEquals(feedbackRequest.getCourse().getTitle(), feedbackRequestDto.getCourseTitle());
        assertEquals(feedbackRequest.getStartDate(), feedbackRequestDto.getStartDate());
        assertEquals(feedbackRequest.getEndDate(), feedbackRequestDto.getEndDate());
        assertEquals(feedbackRequest.isActive(), feedbackRequestDto.isActive());
    }
}
