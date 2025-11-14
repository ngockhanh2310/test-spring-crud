package com.example.hello_spring.dto.response;

public record BookResponseDTO(
        Long id,
        String title,
        String author,
        String isbn,
        int publishedYear
) {
}
