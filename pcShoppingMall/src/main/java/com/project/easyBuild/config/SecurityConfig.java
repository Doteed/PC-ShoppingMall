package com.project.easyBuild.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // CSRF 보호 비활성화 (테스트 환경에서만)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/qnas/**").permitAll() // QnA 페이지 인증 없이 접근 허용
                .anyRequest().permitAll() // 모든 요청 허용
            )
            .formLogin().disable(); // 로그인 폼 비활성화

        return http.build();
    }
}
