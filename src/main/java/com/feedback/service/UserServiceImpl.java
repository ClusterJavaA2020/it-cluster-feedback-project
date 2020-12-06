package com.feedback.service;

import com.feedback.dto.UserDto;
import com.feedback.model.Credentials;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDto login(Credentials credentials) {
        Optional<User> user = userRepo.findByEmail(credentials.getEmail());
        if (user.isPresent() && user.get().getPassword().equals(credentials.getPassword())) {
            UserDto userDto = new UserDto();
            userDto.setEmail(user.get().getEmail());
            userDto.setFirstName(user.get().getFirstName());
            userDto.setLastName(user.get().getLastName());
            userDto.setId(user.get().getId());
            userDto.setPhoneNumber(user.get().getPhoneNumber());
            userDto.setRole(user.get().getRole());
            return userDto;
        }
        return null;
    }

    @Override
    public UserDto register(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        user.setPhoneNumber(userDto.getPhoneNumber());
        userRepo.save(user);
        userDto.setId(user.getId());
        return userDto;
    }


}
