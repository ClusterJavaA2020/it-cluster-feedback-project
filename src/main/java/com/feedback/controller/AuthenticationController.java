package com.feedback.controller;

import com.feedback.config.security.JwtTokenProvider;
import com.feedback.dto.UserAuthenticationDto;
import com.feedback.dto.UserDto;
import com.feedback.repo.entity.User;
import com.feedback.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, AuthenticationService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserAuthenticationDto authenticationDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getPassword()));
            User user = userService.findByEmail(authenticationDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User does not exists"));
            String token = jwtTokenProvider.createToken(authenticationDto.getEmail(), user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("email", authenticationDto.getEmail());
            response.put("token", token);
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        userService.register(userDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/register/confirm/{id}")
    public void confirmEmail(@PathVariable String id) {
        userService.confirmEmail(id);
    }
}
