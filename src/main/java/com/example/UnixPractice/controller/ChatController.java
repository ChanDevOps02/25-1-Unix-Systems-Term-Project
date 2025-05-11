package com.example.UnixPractice.controller;

import com.example.UnixPractice.WebSocket.ChatSessionManager;
import com.example.UnixPractice.entity.ChatMessage;
import com.example.UnixPractice.entity.User;
import com.example.UnixPractice.repository.ChatMessageRepository;
import com.example.UnixPractice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatSessionManager chatSessionManager;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @GetMapping("/chat")
    public String chatPage(Model model) {
        model.addAttribute("title", "실시간 채팅");
        return "chat/chat";
    }

    @GetMapping("/chat/users")
    @ResponseBody
    public List<UserDTO> getOnlineUsers() {
        return chatSessionManager.getAllActiveUsers().stream()
                .map(username -> {
                    Optional<User> optionalUser = userRepository.findByUsername(username);
                    String nickname = optionalUser.map(user ->
                            user.getNickname() != null ? user.getNickname() : username
                    ).orElse(username);
                    return new UserDTO(username, nickname);
                })
                .toList();
    }
    @GetMapping("/chat/history")
    @ResponseBody
    public List<ChatMessage> getChatHistory(@RequestParam String targetUser,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        String currentUser = userDetails.getUsername();
        return chatMessageRepository.findChatHistory(
                currentUser, targetUser, targetUser, currentUser
        );
    }



    @Data
    @AllArgsConstructor
    public static class UserDTO {
        private String id;       // username (실제 전송에 사용)
        private String nickname; // 표시용 닉네임
    }
}
