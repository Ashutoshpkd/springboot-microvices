package com.aip.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcceptedItems {
    private String skuCode;
    private Integer quantity;
}
