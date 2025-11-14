package com.example.hello_spring.dto.auth;

// DTO response token
public record AuthResponse(
        String username,
        String token
) {
}