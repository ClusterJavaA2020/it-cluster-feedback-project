package com.feedback.repo;

import com.feedback.repo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    List<Question> findByIsPatternTrue();

    List<Question> findByIsPatternFalse();

    Question findByQuestionValue(String questionValue);

    @Modifying
    @Query(value = "UPDATE questions SET is_pattern = :isPattern where id = :id",nativeQuery = true)
    @Transactional
    int isPattern(boolean isPattern,Long id);
}
