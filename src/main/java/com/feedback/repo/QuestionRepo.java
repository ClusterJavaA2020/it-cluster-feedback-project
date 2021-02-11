package com.feedback.repo;

import com.feedback.repo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    List<Question> findByPatternTrue();

    List<Question> findByPatternFalse();

    Question findByQuestionValue(String questionValue);

    Set<Question> findByIdIn(Set<Long> idSet);
}
