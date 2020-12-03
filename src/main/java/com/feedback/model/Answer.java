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
    private Long questionId;
    private Long aboutUserId;
    private T answer;
    private String comment;
}
