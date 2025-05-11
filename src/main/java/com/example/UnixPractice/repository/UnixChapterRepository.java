package com.example.UnixPractice.repository;

import com.example.UnixPractice.entity.UnixChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnixChapterRepository extends JpaRepository<UnixChapter, Long> {
    // 기본적인 CRUD는 JpaRepository가 제공해줌
}
