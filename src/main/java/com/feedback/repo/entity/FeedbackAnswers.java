package com.feedback.repo.entity;

import com.feedback.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Document(collection = "answers")
public class FeedbackAnswers {
    @Id
    private String id;
    private Long feedbackRequestId;
    private Set<Answer> answers = new LinkedHashSet<>();
}
