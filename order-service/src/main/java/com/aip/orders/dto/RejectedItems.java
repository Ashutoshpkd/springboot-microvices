package com.aip.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RejectedItems {
    private String skuCode;
    private String rejectionMessage;
}
