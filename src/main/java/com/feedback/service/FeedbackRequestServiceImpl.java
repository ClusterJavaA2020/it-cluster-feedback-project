package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.FeedbackAnswersRepo;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackAnswers;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.springframework.scheduling.annotation.Scheduled;
import com.feedback.util.SwitcherDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import static com.feedback.dto.FeedbackRequestDto.map;

@Service
public class FeedbackRequestServiceImpl implements FeedbackRequestService {
    public static final int END_DATE = 5;
    private final FeedbackRequestRepo feedbackRequestRepo;
    private final CourseRepo courseRepo;
    private final FeedbackAnswersRepo feedbackAnswersRepo;
    private final FeedbackRepo feedbackRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private static final int day = 86400000;

    public FeedbackRequestServiceImpl(UserService userService,UserRepo userRepo,FeedbackRequestRepo feedbackRequestRepo,
                                    CourseRepo courseRepo, FeedbackAnswersRepo feedbackAnswersRepo,
                                      FeedbackRepo feedbackRepo) {
        this.feedbackRequestRepo = feedbackRequestRepo;
        this.courseRepo = courseRepo;
        this.feedbackAnswersRepo = feedbackAnswersRepo;
        this.feedbackRepo = feedbackRepo;
        this.userService = userService;
        this.userRepo=userRepo;
    }

    @Override
    public FeedbackRequestDto createFeedbackRequest(Long courseId) {
        Optional<Course> course = courseRepo.findById(courseId);
        if (course.isPresent()) {
            FeedbackRequest feedbackRequest = feedbackRequestRepo.save(
                    FeedbackRequest.builder()
                            .course(course.get())
                            .startDate(LocalDate.now())
                            .endDate(LocalDate.now().plusDays(END_DATE))
                            .build()
            );
            feedbackAnswersRepo.save(
                    FeedbackAnswers.builder()
                            .feedbackRequestId(feedbackRequest.getId())
                            .answers(new LinkedHashSet<>())
                            .build()
            );
            return map(feedbackRequest);
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

    @Override
    public FeedbackRequestDto activateFeedbackRequest(Long courseId, Long feedbackRequestId, SwitcherDto switcherDto) {
        Optional<Course> course = courseRepo.findById(courseId);
        Optional<FeedbackRequest> feedbackRequest = feedbackRequestRepo.findById(feedbackRequestId);
        Optional<FeedbackAnswers> feedbackAnswers = Optional.ofNullable(feedbackAnswersRepo.findByFeedbackRequestId(feedbackRequestId));
        if (course.isPresent() && feedbackRequest.isPresent() && !feedbackRequest.get().isActive() &&
                feedbackRequest.get().getCourse().getId().equals(courseId)) {
            List<Feedback> feedbackList = new ArrayList<>();
            course.get().getUsers().stream().filter(user -> user.getRole().equals(Role.USER)).forEach(user -> feedbackList.add(
                    Feedback.builder()
                            .userId(user.getId())
                            .feedbackRequestId(feedbackRequest.map(FeedbackRequest::getId).orElse(null))
                            .isActive(true)
                            .answers(feedbackAnswers.map(FeedbackAnswers::getAnswers).orElse(new LinkedHashSet<>()))
                            .build()
                    )
            );
            feedbackRepo.saveAll(feedbackList);
            feedbackRequest.get().setActive(true);
            return map(feedbackRequestRepo.save(feedbackRequest.get()));
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteFeedbackRequest(Long courseId, Long feedbackRequestId) {
        FeedbackRequest feedbackRequest = feedbackRequestRepo.findById(feedbackRequestId).orElse(null);
        if (feedbackRequest != null && feedbackRequest.getCourse().getId().equals(courseId)) {
            feedbackRequestRepo.delete(feedbackRequest);
            FeedbackAnswers feedbackAnswers = feedbackAnswersRepo.findByFeedbackRequestId(feedbackRequestId);
            List<Feedback> feedbackList = feedbackRepo.findByFeedbackRequestId(feedbackRequestId);
            if (!feedbackList.isEmpty()) {
                feedbackRepo.deleteAll(feedbackList);
            }
            if (feedbackAnswers != null) {
                feedbackAnswersRepo.delete(feedbackAnswers);
            }
            return new ResponseEntity<>("REMOVED", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("WRONG PARAMETERS", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Scheduled(fixedDelay = day)
    public void reminder(){
        feedbackRepo.findByIsActiveTrueAndIsSubmittedFalse()
                .stream().map(Feedback::getUserId).forEach(userId->{
            Optional<User> user = userRepo.findById(userId);
            user.ifPresent(userService::sendQuestionnaire);
        });
    }
}
