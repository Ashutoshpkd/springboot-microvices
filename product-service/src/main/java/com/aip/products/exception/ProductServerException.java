package com.aip.products.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ProductServerException extends RuntimeException {
    private String message;

    private HttpStatus status;

    public ProductServerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
