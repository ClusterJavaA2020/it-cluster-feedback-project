package com.feedback.dto;

import com.feedback.repo.entity.Feedback;
import com.feedback.repo.entity.FeedbackRequest;
import com.feedback.repo.entity.Question;
import com.feedback.repo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class FeedbackDto {
    private String id;
    private BriefUserDto user;
    private Long courseId;
    private BriefFeedbackRequestDto feedbackRequest;
    private boolean active;
    private boolean submitted;
    private LocalDateTime date;
    private Set<AnswerDto> answers;

    public static List<FeedbackDto> map(List<Feedback> feedbackList, Set<User> userSet,
                                        Set<FeedbackRequest> feedbackRequestSet, Set<Question> questionSet) {
        return feedbackList.stream().map(feedback ->
                FeedbackDto.builder()
                        .id(feedback.getId())
                        .user(userSet.stream().filter(user -> user.getId().equals(feedback.getUserId()))
                                .map(BriefUserDto::map).findFirst().orElse(null))
                        .courseId(feedback.getCourseId())
                        .feedbackRequest(feedbackRequestSet.stream().filter(feedbackRequest ->
                                feedbackRequest.getId().equals(feedback.getFeedbackRequestId()))
                                .map(BriefFeedbackRequestDto::map).findFirst().orElse(null))
                        .date(feedback.getDate())
                        .active(feedback.isActive())
                        .submitted(feedback.isSubmitted())
                        .answers(feedback.getAnswers().stream()
                                .map(answer -> AnswerDto.builder()
                                        .questionId(answer.getQuestionId())
                                        .question(questionSet.stream().filter(question ->
                                                answer.getQuestionId().equals(question.getId())).findFirst()
                                                .map(Question::getQuestionValue).orElse(null))
                                        .teacher(answer.getTeacherId() != null
                                                ? userSet.stream().filter(teacher ->
                                                answer.getTeacherId().equals(teacher.getId()))
                                                .map(BriefUserDto::map).findFirst().orElse(null)
                                                : null)
                                        .rate(answer.getRate())
                                        .comment(answer.getComment())
                                        .build()).collect(Collectors.toCollection(LinkedHashSet::new)))
                        .build())
                .collect(Collectors.toList());
    }
}
