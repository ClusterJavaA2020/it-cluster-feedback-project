package com.feedback.service;

import com.feedback.dto.UserDto;

public interface UserService {
    UserDto update (UserDto userDto);
    void delete (String email);
}
