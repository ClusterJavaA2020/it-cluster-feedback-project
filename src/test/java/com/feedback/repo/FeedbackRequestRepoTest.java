package com.feedback.repo;

import com.feedback.repo.entity.Course;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FeedbackRequestRepoTest {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FeedbackRequestRepo feedbackRequestRepo;
    @Autowired
    private CourseRepo courseRepo;

    @Test
    void testSave() {
        User user = userRepo.save(user());
        Course testCourse = courseRepo.save(course());
        FeedbackRequest feedbackRequest = feedbackRequest(user, testCourse);
        feedbackRequestRepo.save(feedbackRequest);
        Optional<FeedbackRequest> byId = feedbackRequestRepo.findById(feedbackRequest.getId());
        assertTrue(byId.isPresent());
        byId.get().setCourse(testCourse);
        assertEquals(feedbackRequest, byId.get());
    }

    @Test
    void testFindByCourseId() {
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepo.findByCourseIdOrderByIdDesc(1L);
        assertEquals(feedbackRequest(user(), course()).isActive(), feedbackRequestList.get(0).isActive());
        assertEquals(feedbackRequest(user(), course()).getStartDate(), feedbackRequestList.get(0).getStartDate());
        assertEquals(feedbackRequest(user(), course()).getEndDate(), feedbackRequestList.get(0).getEndDate());
    }

    private FeedbackRequest feedbackRequest(User user, Course course) {
        return FeedbackRequest.builder()
                .course(course)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .active(true)
                .build();
    }

    private User user() {
        return User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .email("email2@mail.com")
                .role(Role.ADMINISTRATOR)
                .build();
    }

    private Course course() {
        return Course.builder().
                title("Test course")
                .description("Test description")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(10))
                .build();
    }

}
