package com.feedback.service;

import com.feedback.dto.UserDto;
import com.feedback.model.Credentials;

public interface UserService {

    public UserDto login(Credentials credentials);
    public UserDto register(UserDto userModel);

}
