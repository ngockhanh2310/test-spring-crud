package com.example.hello_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskCreateDTO(
        @NotBlank(message = "Title cannot be blank")
        @Size(min = 3, message = "Title must be at least 3 characters long")
        String title,
        String description
) {
}
