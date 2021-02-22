package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.dto.UserDto;
import com.feedback.exceptions.UserAlreadyExistException;
import com.feedback.exceptions.UserNotFoundException;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Question;
import com.feedback.repo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.feedback.dto.FeedbackDto.map;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final FeedbackRepo feedbackRepo;
    private final QuestionRepo questionRepo;
    private final FeedbackRequestRepo feedbackRequestRepo;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;
    private static final String SECRET_WORD = "id-secret";

    public UserServiceImpl(UserRepo userRepo, FeedbackRepo feedbackRepo, QuestionRepo questionRepo,
                           FeedbackRequestRepo feedbackRequestRepo, EmailSenderService emailSenderService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.feedbackRepo = feedbackRepo;
        this.questionRepo = questionRepo;
        this.feedbackRequestRepo = feedbackRequestRepo;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (userRepo.findUserByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        User user = userRepo.saveAndFlush(UserDto.map(userDto, passwordEncoder.encode(userDto.getPassword())));
        sendRegistrationEmail(user);
        log.info("Registering new user{}", userDto);
        return UserDto.map(user);
    }

    @Override
    public UserDto getUserById(Long userId) {
        log.info("Receiving user by user id{}", userId);
        return userRepo.findById(userId).map(UserDto::map).orElse(null);
    }

    @Override
    public Set<CourseDto> getUserCoursesByUserId(Long userId) {
        log.info("Receiving user courses by user id{}", userId);
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
        log.info("Receiving feedback by user id{} and course id{}", userId, courseId);
        return map(feedbackList, userSet, feedbackRequestSet, questionSet)
                .stream().sorted(Comparator.comparing(FeedbackDto::getDate).reversed())
                .collect(Collectors.toList());
    }

    public Optional<User> findByEmail(String email) {
        log.info("Finding user by email{}", email);
        return userRepo.findUserByEmail(email);
    }

    public void confirmEmail(String id) {
        Hashids hashids = new Hashids(SECRET_WORD);
        User user = userRepo.findById(Long.parseLong(hashids.decodeHex(id))).orElseThrow(UserNotFoundException::new);
        user.setActive(true);
        userRepo.save(user);
        log.info("Setting active status of user{}", id);
    }

    private void sendRegistrationEmail(User user) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        Hashids hashids = new Hashids(SECRET_WORD);
        String id = hashids.encodeHex(user.getId().toString());

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("You are almost registered!");
        simpleMailMessage.setFrom("feedbackapplication.mail@gmail.com");
        simpleMailMessage.setText("Please click on the below link to activate your account. Thank you!" + "http://localhost:8080/api/auth/register/confirm/" + id);
        emailSenderService.sendEmail(simpleMailMessage);
        log.info("Sending registration email to user{}", user);
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
        log.info("Sending questionnaire to user{} email", user);
    }

}
