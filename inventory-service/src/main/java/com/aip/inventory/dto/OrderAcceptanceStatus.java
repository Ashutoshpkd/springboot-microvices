package com.aip.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAcceptanceStatus {
    private String skuCode;
    private boolean isAccepted;
    private String message;
    private Integer quantity;
}
