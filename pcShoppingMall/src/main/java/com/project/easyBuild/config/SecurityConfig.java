package com.project.easyBuild.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/auth/**").hasRole("ADMIN") // 관리자 접근 제한
	            .anyRequest().permitAll() // 나머지는 모두 허용
	        )
	        .formLogin(form -> form
	            .loginPage("/loginform") // 로그인 페이지
	            .permitAll()
	        )
	        .logout(logout -> logout
	            .logoutUrl("/member/logout")
	            .logoutSuccessUrl("/") // 로그아웃 후 홈으로 이동
	        )
	        .csrf().disable(); // CSRF 비활성화 (개발 중에만)
	    return http.build();
	}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
