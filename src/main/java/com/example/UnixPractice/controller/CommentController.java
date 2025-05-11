package com.example.UnixPractice.controller;

import com.example.UnixPractice.entity.Comment;
import com.example.UnixPractice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    // ✅ 댓글 수정 폼
    @GetMapping("/{id}/edit")
    public String showEditCommentForm(@PathVariable Long id, Model model) {
        Comment comment = commentService.getCommentById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!comment.getAuthor().equals(username)) {
            throw new SecurityException("댓글 수정 권한이 없습니다.");
        }

        model.addAttribute("comment", comment);
        model.addAttribute("postId", comment.getPost().getId()); // ✅ Mustache에서 직접 접근 방지용
        model.addAttribute("title", "댓글 수정");

        return "community/editComment";
    }
    @PostMapping("/{id}/edit")
    public String updateComment(@PathVariable Long id,
                                @RequestParam("content") String content) {
        Comment comment = commentService.getCommentById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!comment.getAuthor().equals(username)) {
            throw new SecurityException("댓글 수정 권한이 없습니다.");
        }

        commentService.updateComment(id, content); // ✅ 서비스에서 내용만 업데이트
        Long postId = comment.getPost().getId();

        return "redirect:/community/" + postId;
    }
    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!comment.getAuthor().equals(username)) {
            throw new SecurityException("댓글 삭제 권한이 없습니다.");
        }

        Long postId = comment.getPost().getId(); // 리디렉션을 위해 게시글 ID 저장
        commentService.deleteComment(id);

        return "redirect:/community/" + postId;
    }

}
