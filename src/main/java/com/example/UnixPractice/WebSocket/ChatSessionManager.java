package com.example.UnixPractice.WebSocket;

import com.example.UnixPractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor // ✅ 생성자 자동 생성
public class ChatSessionManager {

    private final UserRepository userRepository;

    private final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    public void register(String userId, WebSocketSession session) {
        sessionMap.put(userId, session);
    }

    public void unregister(String userId) {
        sessionMap.remove(userId);
    }

    public List<String> getAllActiveUsers() {
        return sessionMap.keySet().stream().toList();
    }

    public List<String> getAllActiveNicknames() {
        return sessionMap.keySet().stream()
                .map(username -> userRepository.findByUsername(username)
                        .map(user -> Optional.ofNullable(user.getNickname()).orElse(username)) // 닉네임 없으면 ID 표시
                        .orElse(username)
                )
                .toList();
    }

    public WebSocketSession getSession(String userId) {
        return sessionMap.get(userId);
    }
}
