package com.feedback.dto;

import lombok.Data;

@Data
public class UserAuthenticationDto {
    private String email;
    private String password;
}
