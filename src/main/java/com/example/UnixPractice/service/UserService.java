package com.example.UnixPractice.service;

import com.example.UnixPractice.dto.UserDto;
import com.example.UnixPractice.entity.User;
import com.example.UnixPractice.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserDto userDto) {
        System.out.println("🔥 UserService - registerUser 실행됨! (강제 출력)"); // ✅ 터미널에서 직접 확인

        log.info("📌 UserService - registerUser 실행됨!");

        try {
            if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
                log.error("회원가입 실패: 이미 존재하는 아이디");
                throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
            }

            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                log.error("회원가입 실패: 비밀번호 불일치");
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            User user = User.builder()
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();

            userRepository.save(user);
            System.out.println("✅ 회원가입 성공! (강제 출력)"); // ✅ 터미널에서 직접 확인
            log.info("✅ 회원가입 성공! username={} 저장 완료", userDto.getUsername());

        } catch (Exception e) {
            log.error("❌ 트랜잭션 롤백 발생! 예외 메시지: {}", e.getMessage());
            throw e;
        }
    }
    public boolean isUsernameTaken(String username) {
        boolean exists = userRepository.findByUsername(username).isPresent();
        System.out.println("🔥 아이디 중복 검사: " + username + " -> " + exists);
        return exists;
    }
    public String getNicknameByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getNickname)
                .orElse("알 수 없음");
    }
    public void updateNickname(String username, String nickname) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        user.setNickname(nickname);
        userRepository.save(user);
    }


}
