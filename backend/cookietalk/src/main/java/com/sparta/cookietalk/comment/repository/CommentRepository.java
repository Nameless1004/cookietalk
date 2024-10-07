package com.sparta.cookietalk.comment.repository;

import com.sparta.cookietalk.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository{

    @Query("SELECT c FROM Comment c JOIN FETCH c.authUser JOIN FETCH c.cookie WHERE c.id=:id")
    Optional<Comment> findWithUserAndCookieById(@Param("id") Long id);
}
