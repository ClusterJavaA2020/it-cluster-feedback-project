package com.feedback.service;

import com.feedback.dto.UserDto;
import com.feedback.repo.entity.User;

import java.util.Optional;

public interface UserService {
    UserDto register(UserDto userDto);
    Optional<User> findByEmail(String email);
    void confirmEmail(String email);
}