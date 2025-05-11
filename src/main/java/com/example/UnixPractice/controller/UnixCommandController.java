package com.example.UnixPractice.controller;

import com.example.UnixPractice.entity.CommandHistory;
import com.example.UnixPractice.repository.CommandHistoryRepository;
import com.example.UnixPractice.service.UnixCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller  // ✅ Mustache 렌더링을 위해 @RestController 대신 @Controller 사용
@RequestMapping("/kmcplace")
@RequiredArgsConstructor
@Slf4j
public class UnixCommandController {

    private final UnixCommandService unixCommandService;
    private final CommandHistoryRepository commandHistoryRepository;

    // ✅ 🔥 UNIX 명령어 UI 페이지 반환
    @GetMapping("/command")
    public String showCommandPage(Model model, @AuthenticationPrincipal User user) {
        if (user == null) {
            log.warn("❌ 로그인되지 않은 사용자가 /kmcplace/command 접근 시도");
            return "redirect:/seoultech/login"; // ✅ 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
        }

        log.info("✅ '{}' 사용자가 UNIX 명령어 페이지에 접속", user.getUsername());

        // 🔹 사용자의 실행 기록 불러오기 (최신순)
        List<CommandHistory> historyList = commandHistoryRepository.findByUsernameOrderByTimestampDesc(user.getUsername());

        model.addAttribute("title", "UNIX 명령어 실행");
        model.addAttribute("username", user.getUsername());
        model.addAttribute("historyList", historyList); // 🔥 실행 기록을 모델에 추가
        return "seoultech/command";  // ✅ Mustache 파일명과 정확히 일치해야 함!
    }

    // ✅ 🔥 UNIX 명령어 실행 API (REST API)
    @PostMapping("/command/execute")
    @ResponseBody
    public List<String> executeCommand(@RequestParam String command, @AuthenticationPrincipal User user) {
        log.info("📌 '{}' 사용자가 '{}' 명령어 실행 요청", user.getUsername(), command);

        try {
            List<String> result = unixCommandService.executeCommand(command, user);
            log.info("✅ 명령어 실행 결과: {}", result);
            return result;
        } catch (Exception e) {
            log.error("❌ 명령어 실행 중 오류 발생: {}", e.getMessage());
            return List.of("❌ 명령어 실행 중 오류가 발생했습니다.");
        }
    }


    // ✅ 🔥 실행 기록 초기화 API (REST API)
    @PostMapping("/command/clear")
    @ResponseBody
    public String clearCommandHistory(@AuthenticationPrincipal User user) {
        if (user == null) {
            log.warn("❌ 로그인되지 않은 사용자가 실행 기록 초기화 시도");
            return "❌ 로그인 후 실행 기록을 초기화할 수 있습니다.";
        }

        log.info("🗑 '{}' 사용자의 실행 기록 초기화 요청", user.getUsername());

        try {
            // ✅ DB에서 실행 기록 삭제
            commandHistoryRepository.deleteAllByUsername(user.getUsername());
            log.info("✅ '{}' 사용자의 실행 기록이 DB에서 삭제됨", user.getUsername());
            return "✅ 실행 기록이 초기화되었습니다.";
        } catch (Exception e) {
            log.error("❌ 실행 기록 초기화 실패: {}", e.getMessage());
            return "❌ 실행 기록 초기화 실패!";
        }
    }
}
