package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.dto.UserDto;
import com.feedback.repo.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    UserDto register(UserDto userDto);

    Optional<User> findByEmail(String email);

    void confirmEmail(String id);

    UserDto getUserById(Long userId);

    Set<CourseDto> getUserCoursesByUserId(Long userId);

    List<FeedbackDto> getFeedbackByUserIdAndCourseId(Long userId, Long courseId);
}
