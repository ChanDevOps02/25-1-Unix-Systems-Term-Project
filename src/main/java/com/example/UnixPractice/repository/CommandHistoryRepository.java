package com.example.UnixPractice.repository;

import com.example.UnixPractice.entity.CommandHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommandHistoryRepository extends JpaRepository<CommandHistory, Long> {

    // ✅ 사용자의 명령어 실행 기록 최신순 조회
    List<CommandHistory> findByUsernameOrderByTimestampDesc(String username);

    // ✅ 실행 기록 삭제 (트랜잭션 적용)
    @Transactional
    void deleteAllByUsername(String username);
}
