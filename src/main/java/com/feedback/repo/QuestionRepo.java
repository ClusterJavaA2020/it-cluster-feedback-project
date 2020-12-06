package com.feedback.repo;

import com.feedback.repo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    Set<Question> findByIsPatternTrue();
}
