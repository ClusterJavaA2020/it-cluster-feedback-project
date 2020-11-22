package com.feedback.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
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
    // 'String' is a temporary type, should be created some specific Class for Answers
    private Set<String> feedbackAnswer;
}
