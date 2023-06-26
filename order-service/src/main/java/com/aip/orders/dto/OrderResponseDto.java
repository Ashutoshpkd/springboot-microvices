package com.aip.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponseDto {
    private String message;
    private String orderNumber;
}
