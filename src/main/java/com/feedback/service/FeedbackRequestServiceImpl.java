package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.dto.UserDto;
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
import com.feedback.util.SwitcherDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.feedback.dto.FeedbackRequestDto.map;

@Slf4j
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

    public FeedbackRequestServiceImpl(UserService userService, UserRepo userRepo, FeedbackRequestRepo feedbackRequestRepo,
                                      CourseRepo courseRepo, FeedbackAnswersRepo feedbackAnswersRepo,
                                      FeedbackRepo feedbackRepo) {
        this.feedbackRequestRepo = feedbackRequestRepo;
        this.courseRepo = courseRepo;
        this.feedbackAnswersRepo = feedbackAnswersRepo;
        this.feedbackRepo = feedbackRepo;
        this.userService = userService;
        this.userRepo = userRepo;
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
            log.info("Creating feedback request for course {}", courseId);
            return map(feedbackRequest);
        } else {
            return null;
        }
    }

    @Override
    public List<FeedbackRequestDto> getFeedbackRequestList(Long courseId) {
        log.info("Receiving list of feedback requests by course id {}", courseId);
        return feedbackRequestRepo.findByCourseIdOrderByIdDesc(courseId)
                .stream().map(FeedbackRequestDto::map).collect(Collectors.toList());
    }

    @Override
    public FeedbackRequestDto getFeedbackRequestById(Long courseId, Long feedbackRequestId) {
        Optional<FeedbackRequest> feedbackRequest = feedbackRequestRepo.findById(feedbackRequestId);
        if (feedbackRequest.isPresent() && feedbackRequest.get().getCourse().getId().equals(courseId)) {
            return map(feedbackRequest.get());
        }
        log.info("Receiving feedback request by id {} for course {}", feedbackRequestId, courseId);
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
                            .courseId(courseId)
                            .feedbackRequestId(feedbackRequest.map(FeedbackRequest::getId).orElse(null))
                            .date(LocalDateTime.now())
                            .active(true)
                            .answers(feedbackAnswers.map(FeedbackAnswers::getAnswers).orElse(new LinkedHashSet<>()))
                            .build()
                    )
            );
            feedbackRepo.saveAll(feedbackList);
            feedbackRequest.get().setActive(switcherDto.isActive());
            return map(feedbackRequestRepo.save(feedbackRequest.get()));
        }
        log.info("Activating feedback request {} for course {}", feedbackRequestId, courseId);
        return null;
    }

    @Override
    public FeedbackRequestDto finishFeedbackRequestSwitcher(Long courseId, Long feedbackRequestId, SwitcherDto switcherDto) {
        Optional<Course> course = courseRepo.findById(courseId);
        Optional<FeedbackRequest> feedbackRequest = feedbackRequestRepo.findById(feedbackRequestId);
        if (course.isPresent() && feedbackRequest.isPresent() && feedbackRequest.get().isActive() &&
                feedbackRequest.get().getCourse().getId().equals(courseId)) {
            feedbackRequest.get().setFinished(switcherDto.isActive());
            List<Feedback> feedbackList = feedbackRepo.findByFeedbackRequestId(feedbackRequestId);
            feedbackList.forEach(feedback -> feedback.setActive(!switcherDto.isActive()));
            feedbackRepo.saveAll(feedbackList);
            return map(feedbackRequestRepo.save(feedbackRequest.get()));
        }
        log.info("Finishing feedback request {} for course {}", feedbackRequestId, courseId);
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
        log.info("Deleting feedback request {} for course {}", feedbackRequestId, courseId);
        return new ResponseEntity<>("WRONG PARAMETERS", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Scheduled(fixedDelay = day)
    public void reminder() {
        feedbackRepo.findByActiveTrueAndSubmittedFalse()
                .stream().map(Feedback::getUserId).forEach(userId -> {
            Optional<User> user = userRepo.findById(userId);
            user.ifPresent(userService::sendQuestionnaire);
        });
        log.info("Sending mail about active feedback request");
    }


    @Override
    @Scheduled(fixedDelay = day)
    public void finishedFeedbackRequests() {
        List<FeedbackRequest> unFinishedFeedbackRequests = feedbackRequestRepo.findByActiveTrueAndFinishedFalse();
        unFinishedFeedbackRequests.forEach(v -> {
            if (v.getEndDate().equals(LocalDate.now())) {
                v.setFinished(true);
                feedbackRequestRepo.save(v);
            }
        });
    }

    @Override
    public Set<UserDto> remindUsersWithoutFeedback(Long courseId, Long feedbackRequestId) {
        Set<Long> userIdSet = feedbackRepo.findByActiveTrueAndSubmittedFalseAndCourseIdAndFeedbackRequestId(courseId, feedbackRequestId)
                .stream().map(Feedback::getUserId).collect(Collectors.toSet());
        Set<User> userSet = userRepo.findByIdIn(userIdSet);
        userSet.forEach(userService::sendQuestionnaire);
        log.info("Reminding users from course{} about feedback request {}", courseId, feedbackRequestId);
        return userSet.stream().map(UserDto::map).collect(Collectors.toSet());
    }
}
