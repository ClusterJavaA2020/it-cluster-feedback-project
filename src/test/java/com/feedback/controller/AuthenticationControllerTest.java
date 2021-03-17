package com.feedback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedback.config.security.JwtTokenProvider;
import com.feedback.dto.UserAuthenticationDto;
import com.feedback.dto.UserDto;
import com.feedback.repo.entity.Role;
import com.feedback.repo.entity.User;
import com.feedback.service.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    AuthenticationService userService;
    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(userService, authenticationManager, jwtTokenProvider);
    }

    @Test
    void testLogin() throws Exception {
        when(authenticationManager.authenticate(any())).thenReturn(any());
        when(userService.findByEmail(userAuthenticationDto().getEmail())).thenReturn(java.util.Optional.ofNullable(user()));
        when(jwtTokenProvider.createToken(userAuthenticationDto().getEmail(), user().getRole().name())).thenReturn("Jwt token");
        MvcResult mvcResult = mockMvc
                .perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userAuthenticationDto())
                        )
                )
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("id", is(user().getId().intValue())))
                .andExpect(jsonPath("email", is(user().getEmail())))
                .andExpect(jsonPath("role", is(user().getRole().toString())))
                .andReturn();
        verify(authenticationManager).authenticate(any());
        verify(userService).findByEmail(userAuthenticationDto().getEmail());
        verify(jwtTokenProvider).createToken(userAuthenticationDto().getEmail(), user().getRole().name());
    }

    @Test
    void testLoginNotValidUser() throws Exception {
        when(authenticationManager.authenticate(any())).thenReturn(any());
        when(userService.findByEmail(userAuthenticationDto().getEmail())).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc
                .perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userAuthenticationDto())
                        )
                )
                .andExpect(status()
                        .isForbidden())
                .andReturn();
        verify(authenticationManager).authenticate(any());
        verify(userService).findByEmail(userAuthenticationDto().getEmail());
    }

    @Test
    void testRegisterUser() throws Exception {
        when(userService.register(userDto())).thenReturn(UserDto.map(user()));
        MvcResult mvcResult = mockMvc
                .perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto())
                        )
                )
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(userService).register(userDto());
    }

    @Test
    void testConfirmUserById() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(get("/auth/register/confirm/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isOk())
                .andReturn();
        verify(userService).confirmEmail(user().getId().toString());
    }

    private UserAuthenticationDto userAuthenticationDto() {
        return UserAuthenticationDto.builder()
                .email("some@example.com")
                .password("12123")
                .build();
    }

    private User user() {
        return User.builder()
                .id(1L)
                .email("some@example.com")
                .firstName("Some")
                .lastName("One")
                .password("12123")
                .phoneNumber("+380")
                .role(Role.USER)
                .build();
    }

    private UserDto userDto() {
        return UserDto.builder()
                .email("some@example.com")
                .firstName("Some")
                .lastName("One")
                .password("12123")
                .phoneNumber("+380")
                .role(Role.USER.toString())
                .build();
    }
}