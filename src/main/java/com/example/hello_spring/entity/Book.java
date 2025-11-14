package com.example.hello_spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, message = "Title must be at least 3 characters long")
    String title;
    String author;
    String isbn; // International Standard Book Number
    @PositiveOrZero(message = "Published year must be a positive number")
    int publishedYear; // Year of publication
}