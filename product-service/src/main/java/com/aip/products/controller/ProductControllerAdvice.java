package com.aip.products.controller;

import com.aip.products.exception.ProductServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ProductControllerAdvice {

    @ExceptionHandler(ProductServerException.class)
    public Mono<ResponseEntity<String>> handleServerException(ProductServerException ex) {
        return Mono.just(ResponseEntity.status(ex.getStatus()).body(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<String>> handleRuntimeException(RuntimeException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage()));
    }

}
