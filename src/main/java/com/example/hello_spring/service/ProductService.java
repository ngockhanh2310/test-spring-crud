package com.example.hello_spring.service;

import com.example.hello_spring.dto.request.ProductRequestDTO;
import com.example.hello_spring.dto.response.ProductResponseDTO;
import com.example.hello_spring.entity.Product;
import com.example.hello_spring.exception.ResourceNotFoundException;
import com.example.hello_spring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    private ProductResponseDTO convertToDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
        );
    }

    private Product findId(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));
    }

    // create a new product
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        log.info("Creating product: {}", productRequestDTO.name());
        Product product = Product.builder()
                .name(productRequestDTO.name())
                .description(productRequestDTO.description())
                .price(productRequestDTO.price())
                .quantity(productRequestDTO.quantity())
                .build();
        return convertToDTO(productRepository.save(product));
    }

    // get all products
    @Transactional(readOnly = true)
    public Iterable<ProductResponseDTO> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    // get product by id
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        log.info("Getting product with id: {}", id);
        Product product = findId(id);
        return convertToDTO(product);
    }

    // update product by id
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        log.info("Updating product with id: {}", id);
        Product product = findId(id);
        product.setName(productRequestDTO.name());
        product.setPrice(productRequestDTO.price());
        product.setQuantity(productRequestDTO.quantity());
        product.setDescription(productRequestDTO.description());
        return convertToDTO(productRepository.save(product));
    }

    // delete product by id
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product with id " + id + " not found");
        }
        log.info("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }
}
