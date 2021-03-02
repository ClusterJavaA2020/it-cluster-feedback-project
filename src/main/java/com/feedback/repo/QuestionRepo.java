package com.feedback.repo;

import com.feedback.repo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    List<Question> findByPatternTrue();

    List<Question> findByPatternFalse();

    Question findByQuestionValue(String questionValue);

    Set<Question> findByIdIn(Set<Long> idSet);

    @Modifying
    @Query(value = "UPDATE questions SET pattern = :pattern where id = :id", nativeQuery = true)
    @Transactional
    void togglePattern(boolean pattern, Long id);
}
