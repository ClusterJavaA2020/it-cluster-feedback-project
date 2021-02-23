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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.feedback.dto.FeedbackRequestDto.map;
import static com.feedback.service.FeedbackRequestServiceImpl.END_DATE;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class FeedbackRequestServiceImplTest {
    @Mock
    private FeedbackRequestRepo feedbackRequestRepo;
    @Mock
    private CourseRepo courseRepo;
    @Mock
    private FeedbackRepo feedbackRepo;
    @Mock
    private FeedbackAnswersRepo feedbackAnswersRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private UserService userService;

    @InjectMocks
    private FeedbackRequestServiceImpl feedbackRequestService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(feedbackRequestRepo, feedbackAnswersRepo, courseRepo, userRepo);
    }

    @Test
    void testCreateFeedbackRequest() {
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course()));
        when(feedbackRequestRepo.save(feedbackRequest())).thenReturn(feedbackRequestDB());
        when(feedbackAnswersRepo.save(feedbackAnswers())).thenReturn(feedbackAnswersDB());
        // NOT_NULL
        FeedbackRequestDto requestDto = feedbackRequestService.createFeedbackRequest(1L);
        assertNotNull(requestDto);
        assertEquals(map(feedbackRequestDB()), requestDto);
        verify(courseRepo).findById(1L);
        verify(feedbackRequestRepo).save(feedbackRequest());
        verify(feedbackAnswersRepo).save(feedbackAnswers());
        // NULL
        FeedbackRequestDto requestDtoNull = feedbackRequestService.createFeedbackRequest(2L);
        assertNull(requestDtoNull);
        verify(courseRepo).findById(2L);
        verify(feedbackRequestRepo).save(feedbackRequest());
    }

    @Test
    void testGetFeedbackRequestList() {
        when(feedbackRequestRepo.findByCourseIdOrderByIdDesc(1L)).thenReturn(List.of(feedbackRequest()));
        List<FeedbackRequestDto> feedbackRequestDto = feedbackRequestService.getFeedbackRequestList(1L);
        feedbackRequestDto.forEach(f -> {
            assertEquals(f.getStartDate(), feedbackRequest().getStartDate());
            assertEquals(f.getEndDate(), feedbackRequest().getEndDate());
            assertEquals(f.getCourseTitle(), feedbackRequest().getCourse().getTitle());
            assertEquals(f.getCourseId(), feedbackRequest().getCourse().getId());
        });
        verify(feedbackRequestRepo).findByCourseIdOrderByIdDesc(1L);
    }

    @Test
    void testGetFeedbackRequestById() {
        when(feedbackRequestRepo.findById(1L)).thenReturn(ofNullable(feedbackRequest()));
        FeedbackRequestDto feedbackRequestDto = feedbackRequestService.getFeedbackRequestById(1L, 1L);
        assertNotNull(feedbackRequestDto);
        assertEquals(feedbackRequestDto.getCourseTitle(), feedbackRequest().getCourse().getTitle());
        assertEquals(feedbackRequestDto.getCourseId(), feedbackRequest().getCourse().getId());
        assertEquals(feedbackRequestDto.getStartDate(), feedbackRequest().getStartDate());
        assertEquals(feedbackRequestDto.getEndDate(), feedbackRequest().getEndDate());
        verify(feedbackRequestRepo).findById(1L);
    }

    @Test
    void testGetFeedbackRequestByIdNegativeCase() {
        when(feedbackRequestRepo.findById(anyLong())).thenReturn(Optional.empty());
        FeedbackRequestDto feedbackRequestDto = feedbackRequestService.getFeedbackRequestById(1L, 1L);
        assertNull(feedbackRequestDto);
        verify(feedbackRequestRepo, times(1)).findById(anyLong());
    }

    @Test
    void testActivateFeedbackRequest() {
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course()));
        when(feedbackRequestRepo.findById(1L)).thenReturn(Optional.of(feedbackRequestDB()));
        when(feedbackAnswersRepo.findByFeedbackRequestId(1L)).thenReturn(feedbackAnswers());
        when(feedbackRequestRepo.save(feedbackRequestDBActive())).thenReturn(feedbackRequestDBActive());
        FeedbackRequestDto feedbackRequestDto =
                feedbackRequestService.activateFeedbackRequest(1L, 1L,
                        SwitcherDto.builder().active(true).build());
        assertNotNull(feedbackRequestDto);
        assertEquals(feedbackRequestDto, map(feedbackRequestDBActive()));
        verify(courseRepo).findById(1L);
        verify(feedbackRequestRepo).findById(1L);
        verify(feedbackAnswersRepo).findByFeedbackRequestId(1L);
        verify(feedbackRequestRepo).save(feedbackRequestDBActive());
    }

    @Test
    void testActivateFeedbackRequestNegativeCase() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());
        when(feedbackRequestRepo.findById(anyLong())).thenReturn(Optional.empty());
        when(feedbackAnswersRepo.findByFeedbackRequestId(anyLong())).thenReturn(feedbackAnswers());
        FeedbackRequestDto feedbackRequestDto =
                feedbackRequestService.activateFeedbackRequest(1L, 1L,
                        SwitcherDto.builder().active(true).build());
        assertNull(feedbackRequestDto);
        verify(courseRepo, times(1)).findById(anyLong());
        verify(feedbackRequestRepo, times(1)).findById(anyLong());
        verify(feedbackAnswersRepo, times(1)).findByFeedbackRequestId(anyLong());
        verify(feedbackRequestRepo, times(0)).save(feedbackRequestDBActive());
    }

    @Test
    void testFinishFeedbackRequestSwitcher() {
        when(courseRepo.findById(1L)).thenReturn(Optional.ofNullable(course()));
        when(feedbackRequestRepo.findById(1L)).thenReturn(Optional.of(feedbackRequestDBActive()));
        when(feedbackRepo.findByFeedbackRequestId(1L)).thenReturn(feedbackList());
        when(feedbackRepo.saveAll(feedbackList())).thenReturn(feedbackList());
        when(feedbackRequestRepo.save(feedbackRequestDBFinished())).thenReturn(feedbackRequestDBFinished());
        FeedbackRequestDto feedbackRequestDto = feedbackRequestService
                .finishFeedbackRequestSwitcher(1L, 1L, SwitcherDto.builder().active(true).build());
        assertNotNull(feedbackRequestDto);
        assertEquals(map(feedbackRequestDBFinished()), feedbackRequestDto);
        verify(courseRepo).findById(1L);
        verify(feedbackRequestRepo).findById(1L);
        verify(feedbackRepo).findByFeedbackRequestId(1L);
        verify(feedbackRepo).saveAll(feedbackList());
        verify(feedbackRequestRepo).save(feedbackRequestDBFinished());
    }

    @Test
    void testFinishFeedbackRequestSwitcherNegativeCase() {
        when(courseRepo.findById(anyLong())).thenReturn(Optional.empty());
        when(feedbackRequestRepo.findById(anyLong())).thenReturn(Optional.empty());
        FeedbackRequestDto feedbackRequestDto = feedbackRequestService
                .finishFeedbackRequestSwitcher(1L, 1L, SwitcherDto.builder().active(true).build());
        assertNull(feedbackRequestDto);
        verify(courseRepo, times(1)).findById(anyLong());
        verify(feedbackRequestRepo, times(1)).findById(anyLong());
        verify(feedbackRepo, times(0)).findByFeedbackRequestId(anyLong());
        verify(feedbackRepo, times(0)).saveAll(feedbackList());
        verify(feedbackRequestRepo, times(0)).save(feedbackRequestDBFinished());
    }

    @Test
    void testDeleteFeedbackRequest() {
        when(feedbackRequestRepo.findById(1L)).thenReturn(Optional.of(feedbackRequestDB()));
        doNothing().when(feedbackRequestRepo).delete(feedbackRequestDB());
        when(feedbackAnswersRepo.findByFeedbackRequestId(1L)).thenReturn(feedbackAnswers());
        when(feedbackRepo.findByFeedbackRequestId(1L)).thenReturn(feedbackList());
        doNothing().when(feedbackRepo).deleteAll(feedbackList());
        doNothing().when(feedbackAnswersRepo).delete(feedbackAnswers());
        ResponseEntity<String> feedbackRequestDto = feedbackRequestService.deleteFeedbackRequest(1L, 1L);
        assertEquals(new ResponseEntity<>("REMOVED", HttpStatus.NO_CONTENT), feedbackRequestDto);
        verify(feedbackRequestRepo).findById(1L);
        verify(feedbackRequestRepo).delete(feedbackRequestDB());
        verify(feedbackAnswersRepo).findByFeedbackRequestId(1L);
        verify(feedbackRepo).findByFeedbackRequestId(1L);
        verify(feedbackRepo).deleteAll(feedbackList());
        verify(feedbackAnswersRepo).delete(feedbackAnswers());
    }

    @Test
    void testDeleteFeedbackRequestNegativeCase() {
        when(feedbackRequestRepo.findById(anyLong())).thenReturn(Optional.of(feedbackRequestDB()));
        ResponseEntity<String> feedbackRequestDto = feedbackRequestService.deleteFeedbackRequest(2L, 1L);
        assertEquals(new ResponseEntity<>("WRONG PARAMETERS", HttpStatus.BAD_REQUEST), feedbackRequestDto);
        verify(feedbackRequestRepo, times(1)).findById(anyLong());
        verify(feedbackRequestRepo, times(0)).delete(feedbackRequestDB());
        verify(feedbackAnswersRepo, times(0)).findByFeedbackRequestId(anyLong());
        verify(feedbackRepo, times(0)).findByFeedbackRequestId(anyLong());
        verify(feedbackRepo, times(0)).deleteAll(feedbackList());
        verify(feedbackAnswersRepo, times(0)).delete(feedbackAnswers());
    }

    @Test
    void testRemindUsersWithoutFeedback() {
        when(feedbackRepo.findByActiveTrueAndSubmittedFalseAndCourseIdAndFeedbackRequestId(1L, 1L))
                .thenReturn(List.of(feedback()));
        when(userRepo.findByIdIn(Set.of(100L))).thenReturn(Set.of(user()));
        doNothing().when(userService).sendQuestionnaire(user());
        Set<UserDto> userSet = feedbackRequestService.remindUsersWithoutFeedback(1L, 1L);
        assertEquals(Set.of(UserDto.map(user())), userSet);
        verify(feedbackRepo).findByActiveTrueAndSubmittedFalseAndCourseIdAndFeedbackRequestId(1L, 1L);
        verify(userRepo).findByIdIn(Set.of(100L));
        verify(userService).sendQuestionnaire(user());

    }

    private Set<User> setOfUsers() {
        return Set.of(
                User.builder()
                        .id(1L)
                        .firstName("FirstUserName")
                        .lastName("FirstLastName")
                        .email("first@mail.com")
                        .role(Role.USER)
                        .password("12345")
                        .build(),
                User.builder()
                        .id(2L)
                        .firstName("SecondUserName")
                        .lastName("SecondLastName")
                        .email("second@mail.com")
                        .role(Role.USER)
                        .password("password")
                        .build()
        );
    }

    private User user() {
        return User.builder()
                .id(100L)
                .firstName("FirstUserName")
                .lastName("FirstLastName")
                .email("single@mail.com")
                .role(Role.USER)
                .password("12345")
                .build();
    }

    private Course course() {
        return Course.builder()
                .id(1L)
                .title("Mock course")
                .description("This is the test course.")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(4))
                .users(setOfUsers())
                .build();
    }

    private FeedbackRequest feedbackRequest() {
        return FeedbackRequest.builder()
                .course(course())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(END_DATE))
                .build();
    }

    private FeedbackRequest feedbackRequestDB() {
        FeedbackRequest feedbackRequest = feedbackRequest();
        feedbackRequest.setId(1L);
        return feedbackRequest;
    }

    private FeedbackRequest feedbackRequestDBActive() {
        FeedbackRequest feedbackRequest = feedbackRequestDB();
        feedbackRequest.setActive(true);
        return feedbackRequest;
    }

    private FeedbackRequest feedbackRequestDBFinished() {
        FeedbackRequest feedbackRequest = feedbackRequestDBActive();
        feedbackRequest.setFinished(true);
        return feedbackRequest;
    }

    private FeedbackAnswers feedbackAnswers() {
        return FeedbackAnswers.builder()
                .feedbackRequestId(1L)
                .answers(new LinkedHashSet<>())
                .build();
    }

    private FeedbackAnswers feedbackAnswersDB() {
        FeedbackAnswers feedbackAnswers = feedbackAnswers();
        feedbackAnswers.setId("700cd3b5cbeda2185b994b0b");
        return feedbackAnswers;
    }

    private List<Feedback> feedbackList() {
        Set<User> userSet = setOfUsers();
        List<Feedback> feedbackList = new ArrayList<>();
        userSet.forEach(user -> {
            feedbackList.add(Feedback.builder()
                    .submitted(true)
                    .courseId(1L)
                    .feedbackRequestId(1L)
                    .userId(user.getId())
                    .answers(feedbackAnswersDB().getAnswers())
                    .build());
        });
        return feedbackList;
    }

    private Feedback feedback() {
        return Feedback.builder()
                .active(true)
                .courseId(1L)
                .feedbackRequestId(1L)
                .userId(100L)
                .answers(feedbackAnswersDB().getAnswers())
                .build();
    }

}
