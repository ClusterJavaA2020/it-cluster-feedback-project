package com.feedback.controller;

import com.feedback.dto.UserDto;
import com.feedback.model.Credentials;
import com.feedback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody Credentials credentials) {
        UserDto user = userService.login(credentials);
        HttpStatus status;
        if (user != null) status = HttpStatus.OK;
        else status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(user, status);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.register(userDto), HttpStatus.OK);
    }


}
