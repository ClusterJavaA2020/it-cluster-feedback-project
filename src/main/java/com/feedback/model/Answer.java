package com.feedback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer {
    private Long questionId;
    private Long teacherId;
    private Integer rate;
    private String comment;
}
