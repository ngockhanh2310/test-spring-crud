package com.example.hello_spring.dto.auth;

// DTO Login
public record AuthRequest(
        String username,
        String password
) {
}
