package com.feedback.controller;

import com.feedback.dto.UserDto;
import com.feedback.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping()
    @PreAuthorize("hasAnyAuthority('user:write','admin:create')")
    public UserDto update(@RequestBody UserDto userDto){
        return userService.update(userDto);
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasAuthority('admin:create')")
    public void delete(@PathVariable String email){userService.delete(email);}

}
