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
public class CommandHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키

    private String username; // 실행한 사용자

    private String command; // 실행한 명령어

    @Column(length = 2000) // 실행 결과를 길게 저장할 수 있도록 설정
    private String result;

    private LocalDateTime timestamp; // 실행 시간
}
