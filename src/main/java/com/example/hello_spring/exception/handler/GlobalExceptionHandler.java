package com.example.hello_spring.exception.handler;

import com.example.hello_spring.dto.response.ErrorResponseDTO;
import com.example.hello_spring.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ResourceNotFoundException.class})
    public ErrorResponseDTO handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ErrorResponseDTO(e.getMessage());
    }
}
