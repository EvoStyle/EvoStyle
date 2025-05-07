package com.example.evostyle.domain.auth.controller;

import com.example.evostyle.domain.auth.dto.request.LoginRequest;
import com.example.evostyle.domain.auth.dto.request.SignUpRequest;
import com.example.evostyle.domain.auth.dto.response.LoginResponse;
import com.example.evostyle.domain.auth.dto.response.SignUpResponse;
import com.example.evostyle.domain.auth.service.AuthService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@RequestBody SignUpRequest request) {
        SignUpResponse signup = authService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(signup);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        LoginResponse loginResponse = authService.refreshAccessToken(refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Long>> logout(
        @RequestHeader("Authorization") String refreshToken,
        @AuthenticationPrincipal AuthUser authUser
    ) {
        Long memberId = authUser.memberId();

        authService.logout(refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("memberId", memberId));
    }
}
