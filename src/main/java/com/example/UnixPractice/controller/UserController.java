package com.example.UnixPractice.controller;

import com.example.UnixPractice.dto.UserDto;
import com.example.UnixPractice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j // ✅ Lombok이 자동으로 Logger 생성!
public class UserController {
    private final UserService userService;

    // 로그인 페이지
    @GetMapping("/kmcplace/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("title", "로그인 페이지");
        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
        }
        return "seoultech/login";
    }

    @GetMapping("/kmcplace/newUser")
    public String newUserPage(Model model) {
        model.addAttribute("title", "회원가입 페이지"); // 🔥 타이틀 추가
        return "seoultech/newUser";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Valid UserDto userDto, Model model) {
        model.addAttribute("title", "회원가입 페이지"); // 🔥 타이틀 추가

        log.info("📌 회원가입 요청이 컨트롤러까지 도착함!");

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            log.error("❌ 회원가입 실패: 비밀번호가 일치하지 않음");
            model.addAttribute("error", "비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return "seoultech/newUser";
        }

        if (userService.isUsernameTaken(userDto.getUsername())) {
            log.error("❌ 회원가입 실패: 이미 존재하는 계정");
            model.addAttribute("error", "이미 존재하는 계정입니다.");
            return "seoultech/newUser";
        }

        try {
            userService.registerUser(userDto);
            log.info("✅ 회원가입 성공! 사용자: {}", userDto.getUsername());
            return "redirect:/seoultech/login";
        } catch (Exception e) {
            log.error("❌ 회원가입 실패! 예외 발생: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "seoultech/newUser";
        }
    }

}
