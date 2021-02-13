package com.feedback.dto;

import com.feedback.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AnswerDto {
    private Long questionId;
    private String question;
    private BriefUserDto teacher;
    private Integer rate;
    private String comment;

    public static Answer map(AnswerDto answerDto) {
        return Answer.builder()
                .questionId(answerDto.getQuestionId())
                .teacherId(answerDto.teacher != null ? answerDto.getTeacher().getId() : null)
                .rate(answerDto.rate)
                .comment(answerDto.getComment())
                .build();
    }
}
