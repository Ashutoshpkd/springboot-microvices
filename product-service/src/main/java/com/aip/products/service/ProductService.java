package com.aip.products.service;

import com.aip.products.domain.Product;
import com.aip.products.dto.ProductRequest;
import com.aip.products.dto.ProductResponse;
import com.aip.products.exception.ProductServerException;
import com.aip.products.repository.ProductReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductReactiveRepository repository;

    public Mono<ProductResponse> saveProduct(ProductRequest request) {
        log.info("Product is being saved {}", request.toString());

        Product product = Product.builder()
                .price(request.getPrice())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .description(request.getDescription())
                .name(request.getName())
                .build();

        return repository.save(product)
                .flatMap(savedProduct -> {
                    if (Objects.isNull(savedProduct)) {
                        return Mono.error(new ProductServerException("Error while saving the product", HttpStatus.INTERNAL_SERVER_ERROR));
                    }
                    return Mono.just(savedProduct);
                })
                .map(this::transformProductResponse);
    }

    private ProductResponse transformProductResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public Mono<List<ProductResponse>> getAllProducts() {
        log.info("Getting all the products");

        return repository.findAll()
                .doOnError(err -> {
                    log.error("Exception occurred while  fetching products {}", err.getMessage());
                    throw new ProductServerException("Something went wrong: " + err.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                })
                .map(this::transformProductResponse)
                .collectList()
                .log();
    }
}
