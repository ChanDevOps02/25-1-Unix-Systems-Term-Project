// ✅ UnixSystemController.java
package com.example.UnixPractice.controller;

import com.example.UnixPractice.entity.UnixChapter;
import com.example.UnixPractice.service.UnixChapterService;
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
@RequestMapping("/subjects/unix")
public class UnixSystemController {

    private final UnixChapterService unixChapterService;

    // ✅ 전체 단원 목록 보기
    @GetMapping
    public String listChapters(Model model) {
        List<UnixChapter> chapters = unixChapterService.getAllChapters();
        model.addAttribute("chapters", chapters);
        model.addAttribute("title", "Unix System 학습");
        return "Unix/list";
    }

    // ✅ 단원 상세 보기
    @GetMapping("/{id}")
    public String viewChapter(@PathVariable Long id, Model model, HttpServletRequest request) {
        UnixChapter chapter = unixChapterService.getChapterById(id)
                .orElseThrow(() -> new IllegalArgumentException("단원을 찾을 수 없습니다."));
        model.addAttribute("chapter", chapter);
        model.addAttribute("title", chapter.getTitle());

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            model.addAttribute("csrfToken", csrf.getToken());
            model.addAttribute("csrfParameterName", csrf.getParameterName());
        }

        return "Unix/detail";
    }


    // ✅ 새 단원 작성 폼
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpServletRequest request) {
        UnixChapter chapter = new UnixChapter();

        model.addAttribute("title", "새 단원 추가");
        model.addAttribute("formAction", "/subjects/unix/new");
        model.addAttribute("titleValue", chapter.getTitle() == null ? "" : chapter.getTitle());
        model.addAttribute("contentValue", chapter.getContent() == null ? "" : chapter.getContent());

        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        if (csrfToken != null) {
            model.addAttribute("csrfParameterName", csrfToken.getParameterName());
            model.addAttribute("csrfToken", csrfToken.getToken());
        }

        return "Unix/form";
    }





    // ✅ 새 단원 저장 처리
    @PostMapping("/new")
    public String createChapter(@RequestParam String title,
                                @RequestParam String content) {
        UnixChapter chapter = new UnixChapter();
        chapter.setTitle(title);
        chapter.setContent(content);
        unixChapterService.saveChapter(chapter);
        return "redirect:/subjects/unix";
    }


    // ✅ 단원 수정 폼
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        UnixChapter chapter = unixChapterService.getChapterById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 단원을 찾을 수 없습니다."));

        model.addAttribute("titleValue", chapter.getTitle());
        model.addAttribute("contentValue", chapter.getContent());
        model.addAttribute("formAction", "/subjects/unix/" + id + "/edit");
        model.addAttribute("title", "단원 수정");

        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        if (csrf != null) {
            model.addAttribute("csrfToken", csrf.getToken());
            model.addAttribute("csrfParameterName", csrf.getParameterName());
        }

        return "Unix/form";
    }

    // ✅ 단원 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteChapter(@PathVariable Long id) {
        unixChapterService.deleteChapter(id);
        return "redirect:/subjects/unix";
    }

}
