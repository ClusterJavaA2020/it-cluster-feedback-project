package com.feedback.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthenticationDto {
    private String email;
    private String password;
}
