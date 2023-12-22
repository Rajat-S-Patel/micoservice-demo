package com.rajat.productservice.service;

import com.rajat.productservice.dto.ProductDto;
import com.rajat.productservice.dto.ProductResDto;
import com.rajat.productservice.model.Product;
import com.rajat.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResDto createProduct(ProductDto productDto) {
        Product product = Product.builder().name(productDto.getName()).description(productDto.getDescription()).price(productDto.getPrice()).build();
        Product savedProduct = productRepository.save(product);
        log.info("Product {} is saved.",savedProduct.getId());
        return mapToProductResponse(product);
    }
    public List<ProductResDto> getAllProducts() {
        return productRepository.findAll().stream().map(this::mapToProductResponse).toList();
    }
    private ProductResDto mapToProductResponse(Product product) {
        return ProductResDto.builder()
                .name(product.getName())
                .id(product.getId())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
