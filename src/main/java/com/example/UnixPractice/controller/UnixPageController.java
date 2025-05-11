package com.example.UnixPractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UnixPageController {

    // UNIX System 페이지 진입
    @GetMapping("/kmcplace/unix")
    public String showUnixSystemPage(Model model) {
        model.addAttribute("title", "Unix System");

        // 나중에 트리 구조나 게시글 리스트를 여기서 모델에 담아줄 수 있음
        return "kmcplace/unix"; // templates/kmcplace/unix.mustache로 렌더링
    }
}
