package com.example.UnixPractice.service;

import com.example.UnixPractice.entity.DigitalLogicChapter;
import com.example.UnixPractice.repository.DigitalLogicChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DigitalLogicChapterService {

    private final DigitalLogicChapterRepository repository;

    public List<DigitalLogicChapter> getAllChapters() {
        return repository.findAll();
    }

    public Optional<DigitalLogicChapter> getChapterById(Long id) {
        return repository.findById(id);
    }

    public void saveChapter(DigitalLogicChapter chapter) {
        repository.save(chapter);
    }

    public void updateChapter(Long id, String title, String content) {
        repository.findById(id).ifPresent(chapter -> {
            chapter.setTitle(title);
            chapter.setContent(content);
            repository.save(chapter);
        });
    }

    public void deleteChapter(Long id) {
        repository.deleteById(id);
    }

}
