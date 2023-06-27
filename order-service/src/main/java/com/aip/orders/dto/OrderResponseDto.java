package com.aip.orders.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponseDto {
    private String message;
    private String orderNumber;
    private List<AcceptedItems> acceptedItems;
    private List<RejectedItems> rejectedItems;
}
