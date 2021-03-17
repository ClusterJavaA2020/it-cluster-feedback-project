package com.feedback.service;

import com.feedback.dto.CourseDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.dto.UserDto;
import com.feedback.repo.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto update(UserDto userDto);

    void delete(String email);

    UserDto getUserById(Long userId);

    Set<CourseDto> getUserCoursesByUserId(Long userId);

    List<FeedbackDto> getFeedbackByUserIdAndCourseId(Long userId, Long courseId);

    void sendQuestionnaire(User user);

    List<UserDto> getAllUsers();
}
