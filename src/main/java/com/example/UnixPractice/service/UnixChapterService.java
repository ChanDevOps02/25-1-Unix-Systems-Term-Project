package com.example.UnixPractice.service;

import com.example.UnixPractice.entity.UnixChapter;
import com.example.UnixPractice.repository.UnixChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnixChapterService {

    private final UnixChapterRepository chapterRepository;

    // 전체 단원 조회
    public List<UnixChapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    // 단일 단원 조회
    public Optional<UnixChapter> getChapterById(Long id) {
        return chapterRepository.findById(id);
    }

    // 단원 저장
    public UnixChapter saveChapter(UnixChapter chapter) {
        return chapterRepository.save(chapter);
    }

    // 단원 삭제
    public void deleteChapter(Long id) {
        chapterRepository.deleteById(id);
    }

    // 단원 수정
    public UnixChapter updateChapter(Long id, String title, String description) {
        UnixChapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 단원을 찾을 수 없습니다."));
        chapter.setTitle(title);
        chapter.setContent(description);
        return chapterRepository.save(chapter);
    }
}
