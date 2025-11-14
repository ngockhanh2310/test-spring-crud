package com.example.hello_spring.controller;

import com.example.hello_spring.dto.request.ProductRequestDTO;
import com.example.hello_spring.dto.response.ProductResponseDTO;
import com.example.hello_spring.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDTO createProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        return productService.createProduct(productRequestDTO);
    }

    @GetMapping
    public Iterable<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductResponseDTO updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        return productService.updateProduct(id, productRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
