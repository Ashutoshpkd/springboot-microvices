package com.aip.inventory.service;

import com.aip.inventory.domain.Inventory;
import com.aip.inventory.dto.InventoryRequestDto;
import com.aip.inventory.dto.InventoryResponseDto;
import com.aip.inventory.dto.OrderAcceptanceStatus;
import com.aip.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;

    public InventoryResponseDto isPresent(List<InventoryRequestDto> orderList) {
        List<String> skuCodes = orderList.stream()
                .map(orderItem -> orderItem.getSkuCode())
                .collect(Collectors.toList());

        List<Inventory> inventories = repository.findBySkuCodeIn(skuCodes);
        Map<String, Inventory> changedInventories = new HashMap<>();
        Map<String, Integer> productAndCount = convertToMap(inventories, changedInventories);
        List<InventoryRequestDto> updatedInventory = new ArrayList<>();
        List<OrderAcceptanceStatus> orderStatuses = getOrderStatus(productAndCount, orderList, updatedInventory, changedInventories);

        updatedInventory.stream().forEach(updatedItem -> {
            repository.bulkUpdateInventory(updatedItem.getSkuCode(),
                    updatedItem.getQuantity());
        });

        return InventoryResponseDto.builder()
                .orderStatuses(orderStatuses)
                .updatedInventory(changedInventories.values().stream().toList())
                .build();
    }

    private List<OrderAcceptanceStatus> getOrderStatus(Map<String, Integer> productAndCount,
                                                       List<InventoryRequestDto> orderList,
                                                       List<InventoryRequestDto> updatedInventory,
                                                       Map<String, Inventory> changedInventories) {
        return orderList.stream().map(order -> {
            if (productAndCount.containsKey(order.getSkuCode())) {
                if (productAndCount.get(order.getSkuCode()) < order.getQuantity()) {
                    return buildFailedOrders(order, "Sorry we only have " + productAndCount.get(order.getSkuCode()) + " items left.");
                }
                updatedInventory.add(
                        InventoryRequestDto.builder()
                                .skuCode(order.getSkuCode())
                                .quantity(productAndCount.get(order.getSkuCode()) - order.getQuantity())
                                .build()
                );
                Inventory updatedItem = changedInventories.get(order.getSkuCode());
                updatedItem.setQuantity(productAndCount.get(order.getSkuCode()) - order.getQuantity());
                changedInventories.put(order.getSkuCode(), updatedItem);
                return buildSuccessOrders(order);
            } else {
                return buildFailedOrders(order, "Sorry we don't have the item you requested");
            }
        }).collect(Collectors.toList());
    }

    private OrderAcceptanceStatus buildFailedOrders(InventoryRequestDto order, String message) {
        return OrderAcceptanceStatus.builder()
                .isAccepted(false)
                .message("Sorry we don't have the item you requested")
                .skuCode(order.getSkuCode())
                .quantity(order.getQuantity())
                .build();
    }

    private OrderAcceptanceStatus buildSuccessOrders(InventoryRequestDto order) {
        return OrderAcceptanceStatus.builder()
                .isAccepted(true)
                .message("Order placed successfully")
                .skuCode(order.getSkuCode())
                .quantity(order.getQuantity())
                .build();
    }

    private Map<String, Integer> convertToMap(List<Inventory> inventories, Map<String, Inventory> changedInventories) {
        Map<String, Integer> productAndCount = new HashMap<>();

        inventories.stream().forEach(inventoryItem ->  {
            productAndCount.put(inventoryItem.getSkuCode(), inventoryItem.getQuantity());
            changedInventories.put(inventoryItem.getSkuCode(), inventoryItem);
        });

        return productAndCount;
    }
}
