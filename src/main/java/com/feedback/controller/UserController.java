package com.feedback.controller;

import com.feedback.dto.UserDto;
import com.feedback.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private UserService userService;

    /*public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping(path = "/register")
    public UserDto registerUser(@RequestBody UserDto userDto){return userService.register(userDto);}
*/
    /*@PutMapping(path = "/update")
    public UserDto updateUser(@RequestBody UserDto userDto){return userService.update(userDto);}
*/

}
