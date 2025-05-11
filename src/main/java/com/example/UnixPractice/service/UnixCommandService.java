package com.example.UnixPractice.service;

import com.example.UnixPractice.entity.CommandHistory;
import com.example.UnixPractice.repository.CommandHistoryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnixCommandService {

    private final CommandHistoryRepository commandHistoryRepository;

    // 🚨 차단할 위험한 명령어 리스트
    private static final List<String> BLOCKED_COMMANDS = Arrays.asList("rm", "shutdown", "reboot", "halt", "poweroff");

    /**
     * ✅ 차단된 명령어인지 검사
     */
    public boolean isSafeCommand(String command) {
        return BLOCKED_COMMANDS.stream().noneMatch(command::contains);
    }

    /**
     * ✅ 사용자의 명령어 실행 후 결과 반환 + DB 저장
     */
    @Transactional
    public List<String> executeCommand(String command, User user) {
        log.info("📌 '{}' 사용자가 명령어 실행: {}", user.getUsername(), command);

        List<String> output = new ArrayList<>();

        // 🚨 보안 검사: 위험한 명령어 실행 차단
        if (!isSafeCommand(command)) {
            log.warn("🚨 '{}' 사용자가 차단된 명령어 실행 시도: {}", user.getUsername(), command);
            return List.of("🚫 차단된 명령어입니다.");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder resultBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
                resultBuilder.append(line).append("\n");
            }

            process.waitFor();

            // ✅ 실행된 명령어 로그 출력
            log.info("✅ 실행 결과: {}", resultBuilder.toString().trim());

            // ✅ 실행된 명령어를 DB에 저장
            CommandHistory history = CommandHistory.builder()
                    .username(user.getUsername())
                    .command(command)
                    .result(resultBuilder.toString().trim()) // 결과 저장
                    .timestamp(LocalDateTime.now()) // 실행 시간 저장
                    .build();

            commandHistoryRepository.save(history); // 🚨 여기서 데이터 저장
            log.info("✅ '{}' 사용자의 명령어 실행 기록이 DB에 저장됨!", user.getUsername());

        } catch (Exception e) {
            log.error("❌ 명령어 실행 중 오류 발생: {}", e.getMessage());
            output.add("오류 발생: " + e.getMessage());
        }

        return output;
    }
    @PostConstruct
    public void checkRepository() {
        log.info("✅ commandHistoryRepository가 주입됨: {}", commandHistoryRepository != null);
    }

}
