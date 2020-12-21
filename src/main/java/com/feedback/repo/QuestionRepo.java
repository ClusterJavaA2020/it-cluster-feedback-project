package com.feedback.repo;

import com.feedback.repo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    List<Question> findByIsPatternTrue();

    List<Question> findByIsPatternFalse();
}
