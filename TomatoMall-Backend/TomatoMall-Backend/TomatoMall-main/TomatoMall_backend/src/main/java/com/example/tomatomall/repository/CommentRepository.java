package com.example.tomatomall.repository;

import com.example.tomatomall.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Modifying
    @Query(value = "UPDATE comment SET son = JSON_ARRAY_APPEND(COALESCE(son, '[]'), '$', :sonId) WHERE id = :fatherId", nativeQuery = true)
    void appendSon(@Param("fatherId") Integer fatherId, @Param("sonId") Integer sonId);
}
