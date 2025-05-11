package com.example.UnixPractice.service;

import com.example.UnixPractice.entity.Post;
import com.example.UnixPractice.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 전체 게시글 조회 (최신순 정렬 등은 필요시 추가)
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 저장
    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    // 게시글 ID로 조회
    @Transactional(readOnly = true)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    // 게시글 삭제
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public void updatePost(Long id, String title, String content) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);
    }

}
