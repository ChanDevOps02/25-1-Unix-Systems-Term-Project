package com.example.UnixPractice.controller;

import com.example.UnixPractice.entity.CommandHistory;
import com.example.UnixPractice.repository.CommandHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HistoryController {

    private final CommandHistoryRepository commandHistoryRepository;

    @GetMapping("/kmcplace/history")
    public String viewCommandHistory(Model model, @AuthenticationPrincipal User user) {
        List<CommandHistory> historyList = commandHistoryRepository.findByUsernameOrderByTimestampDesc(user.getUsername());

        model.addAttribute("title", "명령어 실행 기록");
        model.addAttribute("historyList", historyList);
        return "kmcplace/history";
    }
}
