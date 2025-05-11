package com.example.UnixPractice.repository;

import com.example.UnixPractice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 필요한 경우 커스텀 쿼리 메서드 추가 가능
}
