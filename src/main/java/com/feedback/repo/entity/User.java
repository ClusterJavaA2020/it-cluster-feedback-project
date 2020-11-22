package com.feedback.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    @Column(nullable = false)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_course",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_feedback_request",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "feedback_request_id"))
    private Set<FeedbackRequest> feedbackRequests;
}
