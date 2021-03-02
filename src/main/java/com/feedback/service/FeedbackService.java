package com.feedback.service;

import com.feedback.dto.AnswerDto;
import com.feedback.dto.FeedbackCounterDto;
import com.feedback.dto.FeedbackDto;
import com.feedback.model.Answer;
import com.feedback.repo.entity.Feedback;

import java.util.List;

public interface FeedbackService {

    FeedbackDto getFeedbackById(Long courseId, Long feedbackRequestId, String feedbackId);

    List<Answer> updateFeedbackAnswers(Long courseId, Long feedbackRequestId, String feedbackId, List<AnswerDto> answerDtoList);

    List<FeedbackDto> getSubmittedFeedbackByFeedbackRequestId(Long courseId, Long feedbackRequestId);

    List<Feedback> getAllByFeedbackRequestId(int id);

    FeedbackCounterDto getCounterForUser(Long userId, Long courseId);

    FeedbackCounterDto getCounterForAdmin(Long courseId);
}
