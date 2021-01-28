package com.feedback.repo;

import com.feedback.repo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users u WHERE u.id=?1 AND u.role=1", nativeQuery = true)
    Optional<User> findTeacherById(Long id);
    User findUserById(Long id);
}
