package com.example.UnixPractice.controller;

import com.example.UnixPractice.entity.OopChapter;
import com.example.UnixPractice.service.OopChapterService;
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
@RequestMapping("/subjects/oop")
public class OopController {

    private final OopChapterService service;

    @GetMapping
    public String listChapters(Model model) {
        List<OopChapter> chapters = service.getAllChapters();
        model.addAttribute("chapters", chapters);
        model.addAttribute("title", "Object Oriented Programming");
        return "Oop/list";
    }

    @GetMapping("/{id}")
    public String viewChapter(@PathVariable Long id, Model model, HttpServletRequest request) {
        OopChapter chapter = service.getChapterById(id)
                .orElseThrow(() -> new IllegalArgumentException("단원을 찾을 수 없습니다."));
        model.addAttribute("chapter", chapter);
        model.addAttribute("title", chapter.getTitle());

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            model.addAttribute("csrfToken", csrf.getToken());
            model.addAttribute("csrfParameterName", csrf.getParameterName());
        }

        return "Oop/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, HttpServletRequest request) {
        model.addAttribute("title", "새 단원 추가");
        model.addAttribute("formAction", "/subjects/oop/new");
        model.addAttribute("titleValue", "");
        model.addAttribute("contentValue", "");

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            model.addAttribute("csrfParameterName", csrf.getParameterName());
            model.addAttribute("csrfToken", csrf.getToken());
        }

        return "Oop/form";
    }

    @PostMapping("/new")
    public String createChapter(@RequestParam String title,
                                @RequestParam String content) {
        OopChapter chapter = new OopChapter();
        chapter.setTitle(title);
        chapter.setContent(content);
        service.saveChapter(chapter);
        return "redirect:/subjects/oop";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        OopChapter chapter = service.getChapterById(id)
                .orElseThrow(() -> new IllegalArgumentException("단원을 찾을 수 없습니다."));

        model.addAttribute("title", "단원 수정");
        model.addAttribute("formAction", "/subjects/oop/" + id + "/edit");
        model.addAttribute("titleValue", chapter.getTitle());
        model.addAttribute("contentValue", chapter.getContent());

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            model.addAttribute("csrfParameterName", csrf.getParameterName());
            model.addAttribute("csrfToken", csrf.getToken());
        }

        return "Oop/form";
    }

    @PostMapping("/{id}/edit")
    public String updateChapter(@PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String content) {
        service.updateChapter(id, title, content);
        return "redirect:/subjects/oop/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteChapter(@PathVariable Long id) {
        service.deleteChapter(id);
        return "redirect:/subjects/oop";
    }

}
