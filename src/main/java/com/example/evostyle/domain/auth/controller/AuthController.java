package com.example.evostyle.domain.auth.controller;

import com.example.evostyle.domain.auth.dto.request.SignUpRequest;
import com.example.evostyle.domain.auth.dto.response.LoginResponse;
import com.example.evostyle.domain.auth.dto.response.SignUpResponse;
import com.example.evostyle.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
