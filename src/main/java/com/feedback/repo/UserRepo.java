package com.feedback.repo;

import com.feedback.repo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users u WHERE u.id=?1 AND u.role=2", nativeQuery = true)
    Optional<User> findStudentById(Long id);

    @Query(value = "SELECT * FROM users u WHERE u.id=?1 AND u.role=1", nativeQuery = true)
    Optional<User> findTeacherById(Long id);

    @Query(value = "SELECT * FROM users u LEFT JOIN user_course uc ON u.id=uc.user_id " +
            "WHERE u.id NOT IN (SELECT user_id FROM user_course WHERE course_id=?1) AND u.role=2", nativeQuery = true)
    Set<User> findStudentsNotFromCourse(Long id);

    @Query(value = "SELECT * FROM users u LEFT JOIN user_course uc ON u.id=uc.user_id " +
            "WHERE u.id NOT IN (SELECT user_id FROM user_course WHERE course_id=?1) AND u.role=1", nativeQuery = true)
    Set<User> findTeachersNotFromCourse(Long id);

    Set<User> findByIdIn(Set<Long> idSet);
}
