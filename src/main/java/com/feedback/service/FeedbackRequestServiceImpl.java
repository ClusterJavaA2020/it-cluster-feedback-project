package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.feedback.dto.FeedbackRequestDto.map;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
    public static final int END_DATE = 5;
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
            // SQL
            Set<User> courseUsers = course.get().getUsers();
            FeedbackRequest feedbackRequest = FeedbackRequest.builder()
                    .course(course.get())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(END_DATE))
                    .build();
            courseUsers.forEach(courseUser -> courseUser.getFeedbackRequests().add(feedbackRequest));
            FeedbackRequest feedbackRequestResult = feedbackRequestRepo.save(feedbackRequest);
            // MONGO
            courseUsers.forEach(courseUser -> {
                        if (courseUser.getRole().equals(Role.USER)) {
                            feedbackRepo.save(Feedback.builder()
                                    .feedbackRequestId(feedbackRequestResult.getId())
                                    .userId(courseUser.getId())
                                    .isClosed(false)
                                    .answer(new HashSet<>())
                                    .build()
                            );
                        }
                    }
            );
            return map(feedbackRequestResult);
        } else {
            return null;
        }
    }

    @Override
    public List<FeedbackRequestDto> getFeedbackRequestList(Long courseId) {
        return feedbackRequestRepo.findByCourseId(courseId);
    }

    @Override
    public FeedbackRequestDto getFeedbackRequestById(Long courseId, Long feedbackRequestId) {
        Optional<FeedbackRequest> feedbackRequest = feedbackRequestRepo.findById(feedbackRequestId);
        if (feedbackRequest.isPresent() && feedbackRequest.get().getCourse().getId().equals(courseId)) {
            return map(feedbackRequest.get());
        }
        return null;
    }
}
