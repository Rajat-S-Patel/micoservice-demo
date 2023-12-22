package com.rajat.productservice.controller;

import com.rajat.productservice.dto.ProductDto;
import com.rajat.productservice.dto.ProductResDto;
import com.rajat.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductDto product) {
        productService.createProduct(product);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
