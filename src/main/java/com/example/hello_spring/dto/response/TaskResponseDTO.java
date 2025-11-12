package com.example.hello_spring.dto.response;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        boolean completed
) {
}
