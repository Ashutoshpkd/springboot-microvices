package com.aip.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InventoryResponseDto {
    private List<OrderAcceptanceStatus> orderStatuses;
    private List<Inventory> updatedInventory;
}
