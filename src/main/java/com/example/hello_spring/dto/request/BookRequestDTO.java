package com.example.hello_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record BookRequestDTO(
        @NotBlank(message = "Title is mandatory")
        @Size(min = 3, message = "Title must be at least 3 characters long")
        String title,
        String author,
        String isbn,
        @PositiveOrZero(message = "Published year must be a positive number")
        int publishedYear
) {
}
