package com.feedback.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "feedback_requests")
public class FeedbackRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date startDate;
    private Date endDate;
    private boolean isActive = false;

    @ManyToMany(mappedBy = "feedbackRequests", fetch = FetchType.LAZY)
    private Set<User> users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
