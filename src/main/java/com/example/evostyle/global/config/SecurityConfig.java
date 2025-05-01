package com.example.evostyle.global.config;

import com.example.evostyle.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // CSRF 비활성화 (JWT 사용 시 필요 없음)
            .csrf(AbstractHttpConfigurer::disable)
            // 세션 비활성화
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .rememberMe(AbstractHttpConfigurer::disable)
            // 경로별 접근 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()  // 로그인, 회원가입 등 인증 없이 허용
                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()  // 상품 조회 전체 허용
                .requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()  // 브랜드 조회 전체 허용
                .requestMatchers("/api/products/**").hasRole("OWNER")  // 상품 관리는 OWNER만 허용
                .requestMatchers("/api/brands/**").hasRole("OWNER")  // 브랜드 관리는 OWNER만 허용
                .anyRequest().authenticated()  // 그 외 요청은 인증 필요
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(customAuthEntryPoint)  // 로그인을 하지 않았을 때
                .accessDeniedHandler(customAccessDeniedHandler)  // 권한이 없을 때
            )
            .build();
    }
}
