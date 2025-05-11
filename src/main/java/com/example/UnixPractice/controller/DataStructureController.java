package com.example.UnixPractice.controller;

import com.example.UnixPractice.entity.DataStructureChapter;
import com.example.UnixPractice.service.DataStructureChapterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/subjects/datastructure")
public class DataStructureController {

    private final DataStructureChapterService dataStructureChapterService;

    @GetMapping
    public String listChapters(Model model) {
        List<DataStructureChapter> chapters = dataStructureChapterService.getAllChapters();
        model.addAttribute("chapters", chapters);
        model.addAttribute("title", "Data Structure 학습");
        return "DataStructure/list";
    }

    @GetMapping("/{id}")
    public String viewChapter(@PathVariable Long id, Model model, HttpServletRequest request) {
        DataStructureChapter chapter = dataStructureChapterService.getChapterById(id)
                .orElseThrow(() -> new IllegalArgumentException("단원을 찾을 수 없습니다."));
        model.addAttribute("chapter", chapter);
        model.addAttribute("title", chapter.getTitle());

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            model.addAttribute("csrfToken", csrf.getToken());
            model.addAttribute("csrfParameterName", csrf.getParameterName());
        }

        return "DataStructure/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, HttpServletRequest request) {
        model.addAttribute("title", "새 단원 추가");
        model.addAttribute("formAction", "/subjects/datastructure/new");
        model.addAttribute("titleValue", "");
        model.addAttribute("contentValue", "");

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            model.addAttribute("csrfParameterName", csrf.getParameterName());
            model.addAttribute("csrfToken", csrf.getToken());
        }

        return "DataStructure/form";
    }

    @PostMapping("/new")
    public String createChapter(@RequestParam String title,
                                @RequestParam String content) {
        DataStructureChapter chapter = new DataStructureChapter();
        chapter.setTitle(title);
        chapter.setContent(content);
        dataStructureChapterService.saveChapter(chapter);
        return "redirect:/subjects/datastructure";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        DataStructureChapter chapter = dataStructureChapterService.getChapterById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 단원을 찾을 수 없습니다."));

        model.addAttribute("title", "단원 수정");
        model.addAttribute("formAction", "/subjects/datastructure/" + id + "/edit");
        model.addAttribute("titleValue", chapter.getTitle());
        model.addAttribute("contentValue", chapter.getContent());

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            model.addAttribute("csrfParameterName", csrf.getParameterName());
            model.addAttribute("csrfToken", csrf.getToken());
        }

        return "DataStructure/form";
    }

    @PostMapping("/{id}/edit")
    public String updateChapter(@PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String content) {
        dataStructureChapterService.updateChapter(id, title, content);
        return "redirect:/subjects/datastructure/" + id;
    }

    // ✅ 단원 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteChapter(@PathVariable Long id) {
        dataStructureChapterService.deleteChapter(id);
        return "redirect:/subjects/datastructure";
    }

}
