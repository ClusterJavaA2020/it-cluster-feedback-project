package com.feedback.service;

import com.feedback.dto.AnswerDto;
import com.feedback.model.Answer;

import java.util.Set;

public interface AnswerService {
    Answer createAnswer(Long courseId, Long feedbackRequestId, Long questionId, Long teacherId);

    Set<AnswerDto> getAnswersByFeedbackRequestId(Long courseId, Long feedbackRequestId);

    Set<AnswerDto> deleteAnswer(Long courseId, Long feedbackRequestId, Long questionId, Long teacherId);
}
