package com.example.UnixPractice.WebSocket;

import com.example.UnixPractice.entity.ChatMessage;
import com.example.UnixPractice.repository.ChatMessageRepository;
import com.example.UnixPractice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatSessionManager sessionManager;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String username = (String) session.getAttributes().get("username");
        if (username != null) {
            sessions.put(username, session);
            sessionManager.register(username, session);
            log.info("🔌 사용자 접속: {} (닉네임: {})", username, getNickname(username));
        }
    }

    private String getUserId(WebSocketSession session) {
        Object username = session.getAttributes().get("username");
        return username != null ? username.toString() : "익명";
    }

    private String getNickname(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getNickname() != null ? user.getNickname() : username)
                .orElse(username);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload(); // 형식: "toUserId|메시지"
        String[] parts = payload.split("\\|", 2);
        if (parts.length < 2) return;

        String toUserId = parts[0];
        String text = parts[1];
        String fromUserId = getUserId(session);
        String fromNickname = getNickname(fromUserId);

        // 💬 메시지 저장 (DB)
        ChatMessage chatMessage = ChatMessage.builder()
                .sender(fromUserId)
                .receiver(toUserId)
                .message(text)
                .timestamp(LocalDateTime.now())
                .read(false)
                .build();

        chatMessageRepository.save(chatMessage);

        // 받는 사용자에게 메시지 전송
        WebSocketSession receiverSession = sessionManager.getSession(toUserId);
        if (receiverSession != null && receiverSession.isOpen()) {
            String jsonToReceiver = objectMapper.writeValueAsString(
                    new OutgoingMessage("other", fromUserId, fromNickname, text)
            );
            receiverSession.sendMessage(new TextMessage(jsonToReceiver));
        }

        // 보내는 사용자에게도 전송 (자기 화면에 반영)
        String jsonToSender = objectMapper.writeValueAsString(
                new OutgoingMessage("self", toUserId, fromNickname, text)
        );
        session.sendMessage(new TextMessage(jsonToSender));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String username = (String) session.getAttributes().get("username");
        sessions.remove(username);
        sessionManager.unregister(username);
        log.info("🔌 사용자 연결 종료: {}", username);
    }

    // 클라이언트에게 전송할 JSON 형식 클래스
    @RequiredArgsConstructor
    static class OutgoingMessage {
        public final String type;       // "self" or "other"
        public final String senderId;   // 보내는 사용자 ID
        public final String nickname;   // 보내는 사람 닉네임
        public final String text;       // 메시지 내용
    }
}
