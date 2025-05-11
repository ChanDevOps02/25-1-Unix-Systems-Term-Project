package com.example.UnixPractice.service;

import com.example.UnixPractice.entity.Comment;
import com.example.UnixPractice.entity.Post;
import com.example.UnixPractice.repository.CommentRepository;
import com.example.UnixPractice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // ✅ 댓글 저장
    @Transactional
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // ✅ 특정 게시글에 달린 모든 댓글 조회
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // ✅ 댓글 단건 조회 (수정/삭제 시 필요할 수 있음)
    @Transactional(readOnly = true)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    // ✅ 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // ✅ 전체 댓글 삭제 (관리자용 등)
    public void deleteAllComments() {
        commentRepository.deleteAll();
    }

    public void saveCommentToPost(Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        comment.setPost(post);
        commentRepository.save(comment);
    }
    // ✅ 댓글 수정
    public void updateComment(Long id, String newContent) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.setContent(newContent);
        commentRepository.save(comment);
    }


}
