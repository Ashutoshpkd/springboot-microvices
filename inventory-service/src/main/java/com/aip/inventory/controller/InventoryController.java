package com.aip.inventory.controller;

import com.aip.inventory.dto.InventoryRequestDto;
import com.aip.inventory.dto.InventoryResponseDto;
import com.aip.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService service;

    @PutMapping
    public ResponseEntity<InventoryResponseDto> isPresent(@RequestBody List<InventoryRequestDto> orderList) {
        InventoryResponseDto inventory =  service.isPresent(orderList);
        return ResponseEntity.ok(inventory);
    }
}
