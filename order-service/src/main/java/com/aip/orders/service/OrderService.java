package com.aip.orders.service;

import com.aip.orders.domain.Order;
import com.aip.orders.domain.OrderLineItems;
import com.aip.orders.dto.OrderLineItemsDto;
import com.aip.orders.dto.OrderRequestDto;
import com.aip.orders.dto.OrderResponseDto;
import com.aip.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    public OrderResponseDto placeOrder(OrderRequestDto orderRequest) {
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItems()
                .stream()
                .map(this::mapToOrderLineItems)
                .collect(Collectors.toList());

        Order order = repository.save(Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItems(orderLineItems)
                .build());

        return mapToOrderResponseDto(order);
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
