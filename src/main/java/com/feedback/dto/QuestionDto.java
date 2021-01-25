package com.feedback.dto;

import com.feedback.repo.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class QuestionDto {
    private Long id;
    private boolean isPattern;
    private String questionValue;

    public static QuestionDto map(Question question) {
        return QuestionDto.builder()
                .id(question.getId())
                .questionValue(question.getQuestionValue())
                .isPattern(question.isPattern())
                .build();
    }

    public static Question map(QuestionDto questionDto) {
        return Question.builder()
                .id(questionDto.getId())
                .questionValue(questionDto.getQuestionValue())
                .isPattern(questionDto.isPattern())
                .build();
    }
}
