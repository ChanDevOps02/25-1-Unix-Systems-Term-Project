package com.example.UnixPractice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // ✅ CSRF 비활성화 (테스트용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/kmcplace/login", "/kmcplace/newUser", "/register").permitAll() // ✅ 로그인 & 회원가입 페이지 허용
                        .requestMatchers("/kmcplace/home").authenticated() // ✅ 로그인 후 홈 페이지 접근 가능
                        .requestMatchers("/kmcplace/command").authenticated() // ✅ UNIX 명령어 실행 페이지는 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/kmcplace/login")  // ✅ 로그인 페이지 지정
                        .loginProcessingUrl("/login")  // ✅ 로그인 요청 URL
                        .defaultSuccessUrl("/kmcplace/home", true)  // ✅ 로그인 후 홈 페이지로 이동
                        .failureUrl("/kmcplace/login?error=true")  // ✅ 로그인 실패 시 로그인 페이지로 이동
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // ✅ 로그아웃 URL
                        .logoutSuccessUrl("/kmcplace/login")  // ✅ 로그아웃 후 로그인 페이지로 이동
                        .permitAll()
                );

        return http.build();
    }

    // ✅ Spring Security 6.1+ 방식 (AuthenticationManagerBean 등록)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
