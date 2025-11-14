package com.example.hello_spring.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, message = "Name must be at least 3 characters long")
        String name,
        String description,
        @PositiveOrZero(message = "Price must be at least 0")
        double price,
        @Min(value = 0, message = "Quantity must be at least 0")
        int quantity
) {
}
