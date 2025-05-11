package com.example.UnixPractice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;    // 보낸 사람 username
    private String receiver;  // 받는 사람 username
    private String message;   // 메시지 내용

    private LocalDateTime timestamp;

    private boolean read; // 읽음 여부
}
