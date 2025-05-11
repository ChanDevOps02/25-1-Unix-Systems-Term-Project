package com.example.UnixPractice.service;

import com.example.UnixPractice.entity.ProbabilityChapter;
import com.example.UnixPractice.repository.ProbabilityChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProbabilityChapterService {

    private final ProbabilityChapterRepository repository;

    public List<ProbabilityChapter> getAllChapters() {
        return repository.findAll();
    }

    public Optional<ProbabilityChapter> getChapterById(Long id) {
        return repository.findById(id);
    }

    public void saveChapter(ProbabilityChapter chapter) {
        repository.save(chapter);
    }

    public void updateChapter(Long id, String title, String content) {
        repository.findById(id).ifPresent(c -> {
            c.setTitle(title);
            c.setContent(content);
            repository.save(c);
        });
    }

    public void deleteChapter(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("해당 단원이 존재하지 않습니다.");
        }
        repository.deleteById(id);
    }

}
