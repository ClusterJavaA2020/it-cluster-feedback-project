package com.feedback.service;

import com.feedback.dto.AnswerDto;
import com.feedback.dto.BriefUserDto;
import com.feedback.dto.CourseDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.dto.UserDto;
import com.feedback.exceptions.UserAlreadyExistException;
import com.feedback.exceptions.UserNotFoundException;
import com.feedback.exceptions.UserNotFoundException;
import com.feedback.model.Answer;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.QuestionRepo;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Question;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.*;

import static com.feedback.dto.UserDto.map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceImplTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private FeedbackRepo feedbackRepo;
    @Mock
    private QuestionRepo questionRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private FeedbackRequestRepo feedbackRequestRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(feedbackRepo, feedbackRequestRepo, questionRepo, userRepo);
    }

    @Test
    void testUpdateUser() {
        when(userRepo.findUserByEmail(any())).thenReturn(Optional.ofNullable(user()));
        when(userRepo.save(map(userDto(), (userDto().getPassword())))).thenReturn(user());
        UserDto userDto = userService.update(map(user()));
        assertNotNull(userDto);
        assertEquals(map(user()), userDto);
        verify(userRepo, times(1)).findUserByEmail(any());
        verify(userRepo, times(1)).save(map(userDto(), (userDto().getPassword())));
    }

    @Test
    void testUpdateNotExistingUser() throws UserNotFoundException {
        when(userRepo.findUserByEmail(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.update(map(user())));
        verify(userRepo, times(1)).findUserByEmail(any());
        verify(userRepo, times(0)).save(map(userDto(), (userDto().getPassword())));
    }

    @Test
    void testDeleteUser() {
        when(userRepo.findUserByEmail(any())).thenReturn(Optional.ofNullable(user()));
        userService.delete(userDto().getEmail());
        verify(userRepo, times(1)).findUserByEmail(any());
        verify(userRepo, times(1)).deleteById(user().getId());
    }

    @Test
    void testDeleteNotExistingUser() {
        when(userRepo.findUserByEmail(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.delete(userDto().getEmail()));
        verify(userRepo, times(1)).findUserByEmail(any());
        verify(userRepo, times(0)).deleteById(user().getId());
    }

    @Test
    void testGetUserById() {
        when(userRepo.findById(2L)).thenReturn(java.util.Optional.ofNullable(student()));
        UserDto userDto = userService.getUserById(2L);
        assertEquals(map(student()), userDto);
        verify(userRepo).findById(2L);
    }

    @Test
    void testGetUserCoursesByUserId() {
        when(userRepo.findById(2L)).thenReturn(java.util.Optional.ofNullable(student()));
        Set<CourseDto> courseDtoSet = userService.getUserCoursesByUserId(2L);
        assertEquals(Set.of(CourseDto.map(course())), courseDtoSet);
        verify(userRepo).findById(2L);
    }

    @Test
    void testGetFeedbackByUserIdAndCourseId() {
        when(feedbackRepo.findByUserIdAndCourseId(2L, 3L)).thenReturn(List.of(feedback()));
        when(userRepo.findByIdIn(setOf(2L, 7L))).thenReturn(setOf(student(), teacher()));
        when(feedbackRequestRepo.findByIdIn(setOf(4L))).thenReturn(setOf(feedbackRequest()));
        when(questionRepo.findByIdIn(setOf(10L))).thenReturn(setOf(question()));
        List<FeedbackDto> feedbackDtoList = userService.getFeedbackByUserIdAndCourseId(2L, 3L);
        assertEquals(List.of(feedbackDto()), feedbackDtoList);
        verify(feedbackRepo).findByUserIdAndCourseId(2L, 3L);
        verify(userRepo).findByIdIn(setOf(2L, 7L));
        verify(feedbackRequestRepo).findByIdIn(setOf(4L));
        verify(questionRepo).findByIdIn(setOf(10L));
    }

    @Test
    void testFindByEmail() {
        when(userRepo.findUserByEmail("student@mail.com")).thenReturn(java.util.Optional.ofNullable(student()));
        User user = userService.findByEmail("student@mail.com").orElse(null);
        assertEquals(student(), user);
        verify(userRepo).findUserByEmail("student@mail.com");
    }

    @Test
    void testSendQuestionnaire() {
        doNothing().when(emailSenderService).sendEmail(any(SimpleMailMessage.class));
        userService.sendQuestionnaire(UserDto.map(userDto(), "12312"));
        verify(emailSenderService, times(1)).sendEmail(any());
    }

    @Test
    void testGetAllUsers() {
        when(userRepo.findAll()).thenReturn(List.of(student()));
        List<UserDto> result = userService.getAllUsers();
        assertEquals(List.of(map(student())), result);
        verify(userRepo).findAll();

    }

    private UserDto userDto() {
        return UserDto.builder()
                .id(1L)
                .email("some@example.com")
                .firstName("Some")
                .lastName("One")
                .password("12123")
                .phoneNumber("+380")
                .role("USER")
                .build();
    }

    private User user() {
        return User.builder()
                .id(1L)
                .email("some@example.com")
                .firstName("Some")
                .lastName("One")
                .password("12123")
                .phoneNumber("+380")
                .role(Role.USER)
                .build();
    }

    private FeedbackRequest feedbackRequest() {
        return FeedbackRequest.builder()
                .id(4L)
                .active(true)
                .build();
    }

    private Feedback feedback() {
        return Feedback.builder()
                .id("6025991aa9c08c230ed6f39a")
                .userId(2L)
                .courseId(3L)
                .feedbackRequestId(4L)
                .active(true)
                .submitted(true)
                .answers(Set.of(answer()))
                .build();
    }

    private Answer answer() {
        return Answer.builder()
                .questionId(10L)
                .teacherId(7L)
                .comment("any comment")
                .rate(7)
                .build();
    }

    private AnswerDto answerDto() {
        return AnswerDto.builder()
                .questionId(question().getId())
                .question(question().getQuestionValue())
                .teacher(BriefUserDto.map(teacher()))
                .rate(answer().getRate())
                .comment(answer().getComment())
                .build();
    }

    private User student() {
        return User.builder()
                .id(2L)
                .role(Role.USER)
                .email("student@mail.com")
                .courses(Set.of(course()))
                .build();
    }

    private User teacher() {
        return User.builder()
                .id(7L)
                .role(Role.TEACHER)
                .email("teacher@mail.com")
                .courses(Set.of(course()))
                .build();
    }

    private Question question() {
        return Question.builder()
                .id(10L)
                .questionValue("question value")
                .pattern(true)
                .build();
    }

    private <T> Set<T> setOf(T... item) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, item);
        return set;
    }

    private FeedbackDto feedbackDto() {
        return FeedbackDto.map(List.of(feedback()),
                setOf(student(),
                        teacher()),
                setOf(feedbackRequest()),
                setOf(question())).get(0);
    }

    private Course course() {
        return Course.builder()
                .id(100L)
                .title("Test course")
                .description("Some description")
                .active(true)
                .build();
    }
}
