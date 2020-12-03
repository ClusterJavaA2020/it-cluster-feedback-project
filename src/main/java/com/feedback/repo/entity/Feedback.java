package com.feedback.repo.entity;

import com.feedback.model.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Document(collection = "feedback")
public class Feedback {
    @Id
    private String id;
    private Long userId;
    private Long feedbackRequestId;
    private boolean isClosed;
    private Set<Answer> answer = new HashSet<>();
}
