package com.aip.products.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@Data
@Builder
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private Double price;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
