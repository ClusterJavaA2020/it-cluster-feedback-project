package com.feedback.controller;

import com.feedback.dto.CourseDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.dto.UserDto;
import com.feedback.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/courses")
    public Set<CourseDto> getUserCoursesByUserId(@PathVariable Long userId) {
        return userService.getUserCoursesByUserId(userId);
    }

    @GetMapping("{userId}/courses/{courseId}")
    public List<FeedbackDto> getFeedbackByUserIdAndCourseId(@PathVariable Long userId, @PathVariable Long courseId) {
        return userService.getFeedbackByUserIdAndCourseId(userId, courseId);
    }
}
