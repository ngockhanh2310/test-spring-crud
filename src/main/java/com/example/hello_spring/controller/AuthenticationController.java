package com.example.hello_spring.controller;

import com.example.hello_spring.dto.auth.AuthRequest;
import com.example.hello_spring.dto.auth.AuthResponse;
import com.example.hello_spring.dto.auth.RegisterRequest;
import com.example.hello_spring.dto.response.ApiResponse;
import com.example.hello_spring.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth") // Đường dẫn cơ sở (base path) chung
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;

    // 1. Endpoint Đăng ký (Register)
    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ApiResponse.<AuthResponse>builder()
                .message("Register successful")
                .data(authService.register(request))
                .build();
    }

    // 2. Endpoint Đăng nhập (Login/Authenticate)
    @PostMapping("/authenticate")
    public ApiResponse<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ) {
        return ApiResponse.<AuthResponse>builder()
                .message("Login successful")
                .data(authService.authenticate(request))
                .build();
    }
}
