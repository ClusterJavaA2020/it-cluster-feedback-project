package com.feedback.dto;

import com.feedback.repo.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    @Id
    private Long id;
    private String title;
    private String description;
    private boolean isActive;
    private LocalDate startDate;
    private LocalDate endDate;

    public static CourseDto map (Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .isActive(course.isActive())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .build();
    }

    public static Course map(CourseDto course) {
        return Course.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .isActive(course.isActive())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .build();
    }
}
