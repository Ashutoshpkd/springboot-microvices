package com.aip.products.controller;

import com.aip.products.dto.ProductRequest;
import com.aip.products.dto.ProductResponse;
import com.aip.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService service;
    
    @PostMapping
    public Mono<ResponseEntity<ProductResponse>> addProduct(@RequestBody ProductRequest product) {
        return service.saveProduct(product)
                .map(savedproduct -> ResponseEntity.status(HttpStatus.CREATED).body(savedproduct));
    }

    @GetMapping
    public Mono<ResponseEntity<List<ProductResponse>>> getAllProducts() {
        return service.getAllProducts()
                .map(products -> ResponseEntity.ok(products));
    }
}
