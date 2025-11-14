package com.example.hello_spring.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class DuplicateException extends IllegalArgumentException {
    private final List<String> errors;

    public DuplicateException(List<String> errors) {
        // Gửi một tin nhắn chung chung cho "cha"
        super("Duplicate data found");
        this.errors = errors;
    }
}
