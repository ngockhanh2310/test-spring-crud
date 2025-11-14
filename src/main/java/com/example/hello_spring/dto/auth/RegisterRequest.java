package com.example.hello_spring.dto.auth;

import jakarta.validation.constraints.NotBlank;

// DTO Signup
public record RegisterRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Email is required")
        String email,
        String password
) {
}
