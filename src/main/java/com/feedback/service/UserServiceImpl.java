package com.feedback.service;

import com.feedback.dto.UserDto;
import com.feedback.exceptions.UserNotFoundException;
import com.feedback.repo.UserRepo;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDto update(UserDto userDto) {
       User user = userRepo.findUserByEmail(userDto.getEmail()).orElseThrow(UserNotFoundException::new);
       user.setFirstName(userDto.getFirstName());
       user.setLastName(userDto.getLastName());
       user.setEmail(userDto.getEmail());
       user.setPhoneNumber(userDto.getEmail());
       user.setRole(Role.valueOf(userDto.getRole()));
       return UserDto.map(userRepo.save(user));
    }

    @Override
    public void delete(String email) {
        userRepo.deleteById(userRepo.findUserByEmail(email).orElseThrow(UserNotFoundException::new).getId());
    }
}
