package com.example.UnixPractice.service;

import com.example.UnixPractice.entity.DataStructureChapter;
import com.example.UnixPractice.repository.DataStructureChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataStructureChapterService {

    private final DataStructureChapterRepository repository;

    public List<DataStructureChapter> getAllChapters() {
        return repository.findAll();
    }

    public Optional<DataStructureChapter> getChapterById(Long id) {
        return repository.findById(id);
    }

    public void saveChapter(DataStructureChapter chapter) {
        repository.save(chapter);
    }

    public void updateChapter(Long id, String title, String content) {
        DataStructureChapter chapter = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("단원을 찾을 수 없습니다."));
        chapter.setTitle(title);
        chapter.setContent(content);
        repository.save(chapter);
    }
    public void deleteChapter(Long id) {
        repository.deleteById(id);
    }

}
