package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static com.feedback.dto.FeedbackRequestDto.map;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
    private final FeedbackRequestRepo feedbackRequestRepo;
    private final CourseRepo courseRepo;
    private final FeedbackRepo feedbackRepo;

    public FeedbackRequestServiceImpl(FeedbackRequestRepo feedbackRequestRepo, CourseRepo courseRepo,
                                      FeedbackRepo feedbackRepo) {
        this.feedbackRequestRepo = feedbackRequestRepo;
        this.courseRepo = courseRepo;
        this.feedbackRepo = feedbackRepo;
    }

    @Override
    public FeedbackRequestDto createFeedbackRequest(Long courseId) {
        Optional<Course> course = courseRepo.findById(courseId);
        if (course.isPresent()) {
            Set<User> users = course.get().getUsers();

            FeedbackRequest feedbackRequest = feedbackRequestRepo.save(FeedbackRequest.builder()
                    .course(course.get())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(5))
                    .build());

            users.forEach(user -> {
                        if (user.getRole().equals(Role.USER)) {
                            feedbackRepo.save(Feedback.builder()
                                    .feedbackRequestId(feedbackRequest.getId())
                                    .userId(user.getId())
                                    .isClosed(false)
                                    .build()
                            );
                        }
                    }
            );

            return map(feedbackRequest);
        } else {
            return null;
        }
    }
}
