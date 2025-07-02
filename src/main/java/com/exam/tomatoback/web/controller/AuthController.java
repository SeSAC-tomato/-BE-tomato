package com.exam.tomatoback.web.controller;

import com.exam.tomatoback.auth.service.AuthService;
import com.exam.tomatoback.web.dto.auth.request.EmailCheckRequest;
import com.exam.tomatoback.web.dto.auth.request.LoginRequest;
import com.exam.tomatoback.web.dto.auth.request.NicknameCheckRequest;
import com.exam.tomatoback.web.dto.auth.request.RegisterRequest;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        service.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        service.login(loginRequest, response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/email")
    public ResponseEntity<?> checkDuplicationEmail(@Valid @ModelAttribute EmailCheckRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(service.emailCheck(request)));
    }

    @GetMapping("/nickname")
    public ResponseEntity<?> checkDuplicationNickname(@Valid @ModelAttribute NicknameCheckRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(service.nicknameCheck(request)));
    }

    @PutMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        service.refresh(request, response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        service.logout(response);
        return ResponseEntity.ok().build();
    }
}
