package com.feedback.repo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive = false;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY)
    private Set<User> users;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FeedbackRequest> feedbackRequests;
}
