package com.aip.inventory.controller;

import com.aip.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService service;

    @GetMapping("/{sku-code}")
    public ResponseEntity<String> isPresent(@PathVariable("sku-code") String skuCode) {
        Boolean isEnough =  service.isPresent(skuCode);
        String message = isEnough ? "Item is present" : "Sorry, out of stock";

        return ResponseEntity.ok(message);
    }
}
