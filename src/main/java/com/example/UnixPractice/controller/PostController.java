package com.example.UnixPractice.controller;

import com.example.UnixPractice.dto.CommentDTO;
import com.example.UnixPractice.entity.Comment;
import com.example.UnixPractice.entity.Post;
import com.example.UnixPractice.service.CommentService;
import com.example.UnixPractice.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    // ✅ 게시글 작성 폼 보여주기
    @GetMapping("/new")
    public String showPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("title", "게시글 작성");
        return "community/form";
    }

    // ✅ 게시글 목록 조회
    @GetMapping
    public String listPosts(Model model) {
        model.addAttribute("title", "커뮤니티");
        model.addAttribute("posts", postService.getAllPosts());
        return "community/list";
    }

    // ✅ 게시글 저장 처리
    @PostMapping("/new")
    public String savePost(@ModelAttribute Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        post.setAuthor(username);
        postService.savePost(post);
        return "redirect:/community";
    }

    // ✅ 게시글 상세 보기
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        List<CommentDTO> commentDtos = commentService.getCommentsByPostId(id).stream()
                .map(comment -> new CommentDTO(
                        comment.getId(),
                        comment.getAuthor(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        username.equals(comment.getAuthor())
                ))
                .toList();

        model.addAttribute("post", post);
        model.addAttribute("title", post.getTitle());
        model.addAttribute("isAuthor", username.equals(post.getAuthor()));
        model.addAttribute("comments", commentDtos);

        return "community/detail";
    }

    // ✅ 게시글 삭제 처리
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/community";
    }

    // ✅ 댓글 등록 처리
    @PostMapping("/{postId}/comments")
    public String submitComment(@PathVariable Long postId,
                                @RequestParam String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        commentService.saveComment(Comment.builder()
                .author(username)
                .content(content)
                .post(postService.getPostById(postId).orElseThrow())
                .build());

        return "redirect:/community/" + postId;
    }

    // ✅ 게시글 수정 폼
    @GetMapping("/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        model.addAttribute("post", post);
        model.addAttribute("title", "게시글 수정");
        return "community/edit";
    }

    // ✅ 게시글 수정 처리
    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post updatedPost) {
        postService.updatePost(id, updatedPost.getTitle(), updatedPost.getContent());
        return "redirect:/community/" + id;
    }

    // 댓글 수정 폼
    @GetMapping("/comments/{id}/edit")
    public String editCommentForm(@PathVariable Long id, Model model) {
        Comment comment = commentService.getCommentById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        model.addAttribute("comment", comment);
        model.addAttribute("title", "댓글 수정");
        return "community/editComment";
    }

    // 댓글 수정 처리
    @PostMapping("/comments/{id}/edit")
    public String updateComment(@PathVariable Long id, @RequestParam String content) {
        Comment comment = commentService.getCommentById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        Long postId = comment.getPost().getId();

        commentService.updateComment(id, content);
        return "redirect:/community/" + postId;
    }

}
