package com.example.UnixPractice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kmcplace")
@RequiredArgsConstructor
@Slf4j
public class UnixHomeController {

    // ✅ 🔥 로그인 후 홈 페이지 반환
    @GetMapping("/home")
    public String showHomePage(Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            log.warn("❌ 로그인되지 않은 사용자가 /kmcplace/home 접근 시도");
            return "redirect:/seoultech/login"; // ✅ 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
        }

        log.info("✅ '{}' 사용자가 홈 페이지에 접속", user.getUsername());

        model.addAttribute("title", "홈");  // 🔥 title 값 추가 (오류 방지)
        model.addAttribute("username", user.getUsername());
        return "seoultech/home";
    }
}
