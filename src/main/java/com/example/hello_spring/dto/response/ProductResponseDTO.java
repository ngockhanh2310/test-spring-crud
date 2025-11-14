package com.example.hello_spring.dto.response;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        double price,
        int quantity
) {
}
