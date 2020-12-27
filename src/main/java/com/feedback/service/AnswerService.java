package com.feedback.service;

import com.feedback.model.Answer;

import java.util.Set;

public interface AnswerService {
    Answer createAnswer(Long feedbackRequestId, Long questionId, Long aboutUserId);

    Set<Answer> getQuestionsByFeedbackRequestId(Long feedbackRequestId);

}
