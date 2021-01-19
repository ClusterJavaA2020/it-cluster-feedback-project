package com.feedback.service;

import com.feedback.dto.FeedbackRequestDto;
import com.feedback.util.SwitcherDto;
import com.feedback.repo.CourseRepo;
import com.feedback.repo.FeedbackRepo;
import com.feedback.repo.FeedbackRequestRepo;
import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.feedback.dto.FeedbackRequestDto.map;
import static com.feedback.service.FeedbackRequestServiceImpl.END_DATE;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @InjectMocks
    private FeedbackRequestServiceImpl feedbackRequestServiceImpl;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(feedbackRequestRepo, feedbackRepo, courseRepo);
    }

    @Test
    void testCreateFeedbackRequest() {
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course()));
        when(feedbackRequestRepo.save(feedbackRequest())).thenReturn(feedbackRequestDB());
        setOfUsers().forEach(user -> when(feedbackRepo.save(feedback(user))).thenReturn(feedbackDB(user)));
        // NOT_NULL
        FeedbackRequestDto requestDto = feedbackRequestServiceImpl.createFeedbackRequest(1L);
        assertNotNull(requestDto);
        assertEquals(requestDto.getCourseTitle(), feedbackRequest().getCourse().getTitle());
        assertEquals(requestDto.getStartDate(), feedbackRequest().getStartDate());
        assertEquals(requestDto.getEndDate(), feedbackRequest().getEndDate());
        assertEquals(requestDto.getId(), feedbackRequestDB().getId());
        verify(courseRepo).findById(1L);
        // NULL
        FeedbackRequestDto requestDtoNull = feedbackRequestServiceImpl.createFeedbackRequest(2L);
        assertNull(requestDtoNull);
        verify(courseRepo).findById(2L);
        verify(feedbackRequestRepo).save(feedbackRequest());
        setOfUsers().forEach(user -> verify(feedbackRepo).save(feedback(user)));
    }

    @Test
    void testGetFeedbackRequestList() {
        when(feedbackRequestRepo.findByCourseId(1L)).thenReturn(List.of(map(feedbackRequest())));
        List<FeedbackRequestDto> feedbackRequestDto = feedbackRequestServiceImpl.getFeedbackRequestList(1L);
        feedbackRequestDto.forEach(f -> {
            assertEquals(f.getStartDate(), feedbackRequest().getStartDate());
            assertEquals(f.getEndDate(), feedbackRequest().getEndDate());
            assertEquals(f.getCourseTitle(), feedbackRequest().getCourse().getTitle());
            assertEquals(f.getCourseId(), feedbackRequest().getCourse().getId());
        });
        verify(feedbackRequestRepo).findByCourseId(1L);
    }

    @Test
    void testGetFeedbackRequestById() {
        when(feedbackRequestRepo.findById(1L)).thenReturn(ofNullable(feedbackRequest()));
        FeedbackRequestDto feedbackRequestDto = feedbackRequestServiceImpl.getFeedbackRequestById(1L, 1L);
        assertNotNull(feedbackRequestDto);
        assertEquals(feedbackRequestDto.getCourseTitle(), feedbackRequest().getCourse().getTitle());
        assertEquals(feedbackRequestDto.getCourseId(), feedbackRequest().getCourse().getId());
        assertEquals(feedbackRequestDto.getStartDate(), feedbackRequest().getStartDate());
        assertEquals(feedbackRequestDto.getEndDate(), feedbackRequest().getEndDate());
        verify(feedbackRequestRepo).findById(1L);
    }

    @Test
    void testUpdateFeedbackRequestActivation() {
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course()));
        when(feedbackRequestRepo.findById(1L)).thenReturn(Optional.of(feedbackRequestDB()));
        when(feedbackRequestRepo.save(feedbackRequestDB())).thenReturn(feedbackRequestDB());
        FeedbackRequestDto feedbackRequestDto =
                feedbackRequestServiceImpl.updateFeedbackRequestActivation(1L, 1L,
                        SwitcherDto.builder().isActive(false).build());
        assertNotNull(feedbackRequestDto);
        assertEquals(feedbackRequestDto.getId(), feedbackRequestDB().getId());
        assertEquals(feedbackRequestDto.getCourseId(), feedbackRequestDB().getId());
        assertEquals(feedbackRequestDto.getCourseTitle(), feedbackRequestDB().getCourse().getTitle());
        assertEquals(feedbackRequestDto.getStartDate(), feedbackRequestDB().getStartDate());
        assertEquals(feedbackRequestDto.getEndDate(), feedbackRequestDB().getEndDate());
        verify(courseRepo).findById(1L);
        verify(feedbackRequestRepo).findById(1L);
        verify(feedbackRequestRepo).save(feedbackRequestDB());
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
                        .feedbackRequests(new HashSet<>())
                        .build(),
                User.builder()
                        .id(2L)
                        .firstName("SecondUserName")
                        .lastName("SecondLastName")
                        .email("second@mail.com")
                        .role(Role.USER)
                        .password("password")
                        .feedbackRequests(new HashSet<>())
                        .build()
        );
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
                .users(setOfUsers())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(END_DATE))
                .build();
    }

    private FeedbackRequest feedbackRequestDB() {
        FeedbackRequest feedbackRequest = feedbackRequest();
        feedbackRequest.setId(1L);
        return feedbackRequest;
    }

    private Feedback feedback(User user) {
        return Feedback.builder()
                .isClosed(false)
                .feedbackRequestId(feedbackRequestDB().getId())
                .userId(user.getId())
                .isClosed(false)
                .answer(new HashSet<>())
                .build();
    }

    private Feedback feedbackDB(User user) {
        Feedback feedback = feedback(user);
        feedback.setId("someId");
        return feedback;
    }
}