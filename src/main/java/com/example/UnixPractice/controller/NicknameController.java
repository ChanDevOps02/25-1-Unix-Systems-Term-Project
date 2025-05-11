// NicknameController.java
package com.example.UnixPractice.controller;

import com.example.UnixPractice.entity.User;
import com.example.UnixPractice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NicknameController {

    private final UserRepository userRepository;

    @GetMapping("/nickname")
    public String nicknameForm(@AuthenticationPrincipal UserDetails userDetails,
                               Model model,
                               HttpServletRequest request) {

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("title", "닉네임 설정");

        // ✅ CSRF 토큰 추가
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        if (token != null) {
            model.addAttribute("csrfParameterName", token.getParameterName());
            model.addAttribute("csrfToken", token.getToken());
        }

        return "user/nickname-form";
    }

    @PostMapping("/nickname")
    public String updateNickname(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String nickname) {

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setNickname(nickname);
        userRepository.save(user);

        return "redirect:/chat"; // 설정 후 홈 화면으로 리다이렉트 (또는 채팅 페이지로 이동도 가능)
    }
}
