package com.example.UnixPractice.service;

import com.example.UnixPractice.entity.DiscreteMathChapter;
import com.example.UnixPractice.repository.DiscreteMathChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscreteMathChapterService {

    private final DiscreteMathChapterRepository repository;

    public List<DiscreteMathChapter> getAllChapters() {
        return repository.findAll();
    }

    public Optional<DiscreteMathChapter> getChapterById(Long id) {
        return repository.findById(id);
    }

    public void saveChapter(DiscreteMathChapter chapter) {
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
