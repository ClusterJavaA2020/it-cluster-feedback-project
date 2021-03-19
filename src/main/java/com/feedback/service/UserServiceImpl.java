
package com.feedback.service;

import com.feedback.dto.UserDto;
import com.feedback.dto.CourseDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.exceptions.UserNotFoundException;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Question;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Collections;
import java.util.Optional;

import static com.feedback.dto.FeedbackDto.map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final FeedbackRepo feedbackRepo;
    private final QuestionRepo questionRepo;
    private final FeedbackRequestRepo feedbackRequestRepo;
    private final EmailSenderService emailSenderService;


    public UserServiceImpl(UserRepo userRepo, FeedbackRepo feedbackRepo, QuestionRepo questionRepo,
                           FeedbackRequestRepo feedbackRequestRepo, EmailSenderService emailSenderService) {
        this.userRepo = userRepo;
        this.feedbackRepo = feedbackRepo;
        this.questionRepo = questionRepo;
        this.feedbackRequestRepo = feedbackRequestRepo;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public UserDto update(UserDto userDto) {
        log.info("Updating user {}", userDto);
        User user = userRepo.findUserByEmail(userDto.getEmail()).orElseThrow(UserNotFoundException::new);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setActive(userDto.isActive());
        return UserDto.map(userRepo.save(user));
    }

    @Override
    public void delete(String email) {
        userRepo.deleteById(userRepo.findUserByEmail(email).orElseThrow(UserNotFoundException::new).getId());
        log.info("Deleting user {}", email);
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Receiving user by user id {}", userId);
        return userRepo.findById(userId).map(UserDto::map).orElse(null);
    }

    @Override
    public Set<CourseDto> getUserCoursesByUserId(Long userId) {
        log.info("Receiving user courses by user id {}", userId);
        return userRepo.findById(userId)
                .map(u -> u.getCourses().stream().map(CourseDto::map)
                        .sorted(Comparator.comparing(CourseDto::getStartDate).reversed())
                        .collect(Collectors.toCollection(LinkedHashSet::new)))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<FeedbackDto> getFeedbackByUserIdAndCourseId(Long userId, Long courseId) {
        List<Feedback> feedbackList = feedbackRepo.findByUserIdAndCourseId(userId, courseId);
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> feedbackRequestIdSet = new HashSet<>();
        Set<Long> questionIdSet = new HashSet<>();
        feedbackList.forEach(feedback -> {
            userIdSet.add(feedback.getUserId());
            feedbackRequestIdSet.add(feedback.getFeedbackRequestId());
            feedback.getAnswers().forEach(answer -> {
                questionIdSet.add(answer.getQuestionId());
                userIdSet.add(answer.getTeacherId());
            });
        });
        Set<User> userSet = userRepo.findByIdIn(userIdSet);
        Set<FeedbackRequest> feedbackRequestSet = feedbackRequestRepo.findByIdIn(feedbackRequestIdSet);
        Set<Question> questionSet = questionRepo.findByIdIn(questionIdSet);
        log.info("Receiving feedback by user id {} and course id {}", userId, courseId);
        List<FeedbackDto> result = map(feedbackList, userSet, feedbackRequestSet, questionSet)
                .stream().sorted(Comparator.comparing(v -> v.getFeedbackRequest().getEndDate()))
                .collect(Collectors.toList());
        Collections.reverse(result);
        return result;
    }

    public Optional<User> findByEmail(String email) {
        log.info("Finding user by email {}", email);
        return userRepo.findUserByEmail(email);
    }

    @Override
    public void sendQuestionnaire(User user) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("form");
        simpleMailMessage.setFrom("feedbackapplication.mail@gmail.com");
        //user page is in process
        simpleMailMessage.setText("please respond on a small questionnaire " + "http://localhost:8080/api/auth/findUserById/" + user.getId());
        emailSenderService.sendEmail(simpleMailMessage);
        log.info("Sending questionnaire to users {} email", user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream().map(UserDto::map)
                .sorted(Comparator.comparing(UserDto::getLastName))
                .collect(Collectors.toList());
    }
}
