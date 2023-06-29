package com.aip.orders.service;

import com.aip.orders.domain.Order;
import com.aip.orders.domain.OrderLineItems;
import com.aip.orders.dto.*;
import com.aip.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private OrderRepository repository;

    public OrderResponseDto placeOrder(OrderRequestDto orderRequest) {

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItems()
                .stream()
                .map(this::mapToOrderLineItems)
                .collect(Collectors.toList());

        List<InventoryRequestDto> orderItems = orderRequest.getOrderLineItems()
                .stream()
                .map(lineItem -> InventoryRequestDto.builder()
                        .quantity(lineItem.getQuantity())
                        .skuCode(lineItem.getSkuCode())
                        .build())
                .collect(Collectors.toList());

        InventoryResponseDto inventory = webClientBuilder.build().put()
                .uri("http://inventory-service/api/inventory")
                .bodyValue(orderItems)
                .retrieve()
                .bodyToMono(InventoryResponseDto.class)
                .block();

        log.info("Retrived inventoryList {}", inventory.getUpdatedInventory());

        return constructOrderResponse(inventory);
    }

    private OrderResponseDto constructOrderResponse(InventoryResponseDto inventory) {
        Boolean isAnyItemPresent = inventory.getOrderStatuses().stream().anyMatch(this::isAnyItemPresent);
        List<RejectedItems> rejectedItemsList = inventory.getOrderStatuses()
                .stream()
                .filter(listItem -> !listItem.isAccepted())
                .map(this::buildRejectedResponse)
                .toList();

        List<AcceptedItems> acceptedItemsList = inventory.getOrderStatuses()
                .stream()
                .filter(listItem -> listItem.isAccepted())
                .map(this::buildAcceptedResponse)
                .toList();

        if (!isAnyItemPresent) {
            return OrderResponseDto.builder()
                    .message("We are not able to place your order")
                    .rejectedItems(rejectedItemsList)
                    .build();
        }
        return OrderResponseDto.builder()
                .orderNumber(UUID.randomUUID().toString())
                .message("Order placed successfully")
                .acceptedItems(acceptedItemsList)
                .rejectedItems(rejectedItemsList)
                .build();
    }

    private AcceptedItems buildAcceptedResponse(OrderAcceptanceStatus orderAcceptanceStatus) {
        return AcceptedItems.builder()
                .skuCode(orderAcceptanceStatus.getSkuCode())
                .quantity(orderAcceptanceStatus.getQuantity())
                .build();
    }

    private RejectedItems buildRejectedResponse(OrderAcceptanceStatus orderAcceptanceStatus) {
        return RejectedItems.builder()
                .rejectionMessage(orderAcceptanceStatus.getMessage())
                .skuCode(orderAcceptanceStatus.getSkuCode())
                .build();
    }

    private boolean isAnyItemPresent(OrderAcceptanceStatus orderAcceptanceStatus) {
        return orderAcceptanceStatus.isAccepted();
    }

    private OrderLineItems  mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .skuCode(orderLineItemsDto.getSkuCode())
                .build();
    }

    private OrderResponseDto mapToOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .orderNumber(order.getOrderNumber())
                .message("Order placed successfully!")
                .build();
    }
}
