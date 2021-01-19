package com.feedback.repo;

import com.feedback.repo.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionRepoTest {
    @Autowired
    private QuestionRepo questionRepo;

    @Test
    void testFindByIsPatternTrue() {
        Question question = questionList().get(1);
        Question save = questionRepo.save(question);
        List<Question> result = questionRepo.findByIsPatternTrue();
        assertFalse(result.isEmpty());
        assertEquals(save, result.get(0));
    }

    @Test
    void testFindByIsPatternFalse() {
        Question question = questionList().get(0);
        question.setQuestionValue("testFindByIsPatternFalse");
        Question save = questionRepo.save(question);
        List<Question> result = questionRepo.findByIsPatternFalse();
        assertFalse(result.isEmpty());
        assertEquals(save, question);
    }

    @Test
    void testFindByQuestionValue() {
        Question question = questionList().get(0);
        Question save = questionRepo.save(question);
        Question find = questionRepo.findByQuestionValue(questionList().get(0).getQuestionValue());
        assertEquals(save, find);
    }

    private List<Question> questionList() {
        return List.of(Question.builder()
                        .isRateable(true)
                        .isPattern(false)
                        .questionValue("Some first question?")
                        .build(),
                Question.builder()
                        .isRateable(false)
                        .isPattern(true)
                        .questionValue("Some second question?")
                        .build()
        );
    }
}
