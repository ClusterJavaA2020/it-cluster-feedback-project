package com.feedback.dto;

import com.feedback.repo.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class CourseDto {
    private Long id;
    @NotBlank(message = "title is mandatory")
    private String title;
    private String description;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;

    public static CourseDto map(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .active(course.isActive())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .build();
    }

    public static Course map(CourseDto course) {
        return Course.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .active(course.isActive())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .build();
    }
}
