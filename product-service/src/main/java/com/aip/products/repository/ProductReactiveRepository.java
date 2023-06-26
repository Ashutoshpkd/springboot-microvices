package com.aip.products.repository;

import com.aip.products.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductReactiveRepository extends ReactiveMongoRepository<Product, String> {
}
