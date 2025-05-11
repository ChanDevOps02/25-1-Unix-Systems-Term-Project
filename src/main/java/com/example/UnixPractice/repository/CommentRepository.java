package com.example.UnixPractice.repository;

import com.example.UnixPractice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 게시글 ID로 댓글 목록 조회
    List<Comment> findByPostId(Long postId);
}
