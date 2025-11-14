package com.example.hello_spring.controller;

import com.example.hello_spring.dto.request.BookRequestDTO;
import com.example.hello_spring.dto.response.ApiResponse;
import com.example.hello_spring.dto.response.BookResponseDTO;
import com.example.hello_spring.service.BookService;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookController {
    private final BookService bookService;

    @PostMapping("/news")
    public BookResponseDTO createBook(
            @Valid @RequestBody BookRequestDTO requestDTO) {
        return bookService.createBook(requestDTO);
    }

    @GetMapping("/{id}")
    public ApiResponse<BookResponseDTO> getBookById(@PathVariable Long id) {
        return ApiResponse.<BookResponseDTO>builder()
                .message("Book information found")
                .data(bookService.getBookById(id))
                .build();
    }

    @GetMapping("/list")
    public Iterable<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PutMapping("update/{id}")
    public BookResponseDTO updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO requestDTO) {
        return bookService.updateBooks(id, requestDTO);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) {
        String message = bookService.deleteBooks(id);
        return ResponseEntity.ok(new ApiResponse(message));
    }
}
