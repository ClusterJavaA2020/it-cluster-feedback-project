package com.feedback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer<T> {
    private String question;
    private int teacherId;
    private T answer;
    private String comment;
}
