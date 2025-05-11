package com.example.UnixPractice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private boolean isAuthor;
}
