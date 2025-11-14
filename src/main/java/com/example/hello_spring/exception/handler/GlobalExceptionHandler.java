package com.example.hello_spring.exception.handler;

import com.example.hello_spring.dto.response.ApiResponse;
import com.example.hello_spring.exception.DuplicateException;
import com.example.hello_spring.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 1. Handler 404 (Không tìm thấy)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        // Dùng builder, data sẽ tự động là null
        return ApiResponse.builder()
                .message(ex.getMessage())
                .build();
    }

    // 2. Handler 400 (Trùng lặp)
    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<String>> handleDuplicate(DuplicateException ex) {
        return ApiResponse.<List<String>>builder()
                .message("Registration failed due to duplicate data")
                .data(ex.getErrors()) // <-- Lấy danh sách lỗi từ Exception
                .build();
    }

    // 3. Handler 400 (Lỗi Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Thay vì trả về Map, hãy trả về Map đó BÊN TRONG trường "data"
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ApiResponse.builder()
                .message("Validation failed")
                .data(errors) // Đưa Map lỗi vào trường "data"
                .build();
    }
}
