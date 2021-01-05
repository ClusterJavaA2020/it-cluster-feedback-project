package com.feedback.service;

import com.feedback.model.Answer;

import java.util.Set;

public interface AnswerService {
    Answer createAnswer(Long courseId, Long feedbackRequestId, Long questionId, Long teacherId);

    Set<Answer> getAnswersByFeedbackRequestId(Long courseId, Long feedbackRequestId);

}
