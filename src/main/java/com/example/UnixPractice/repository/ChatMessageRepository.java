package com.example.UnixPractice.repository;

import com.example.UnixPractice.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE (m.sender = :sender1 AND m.receiver = :receiver1) " +
            "   OR (m.sender = :sender2 AND m.receiver = :receiver2) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(
            @Param("sender1") String sender1,
            @Param("receiver1") String receiver1,
            @Param("sender2") String sender2,
            @Param("receiver2") String receiver2
    );
}
