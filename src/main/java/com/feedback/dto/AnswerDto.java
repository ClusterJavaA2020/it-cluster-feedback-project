package com.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AnswerDto {
    private String question;
    private UserDto teacher;
    private Integer rate;
    private String comment;
}
