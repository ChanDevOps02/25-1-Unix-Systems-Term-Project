package com.example.UnixPractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/kmcplace/about")
    public String aboutPage(Model model) {
        model.addAttribute("title", "개발자 소개");
        return "seoultech/about";
    }
}
