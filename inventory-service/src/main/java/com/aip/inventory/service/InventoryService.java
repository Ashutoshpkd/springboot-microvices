package com.aip.inventory.service;

import com.aip.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;

    public boolean isPresent(String skuCode) {
        return repository.findBySkuCode(skuCode).isPresent();
    }
}
