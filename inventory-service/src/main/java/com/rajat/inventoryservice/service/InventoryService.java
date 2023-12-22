package com.rajat.inventoryservice.service;

import com.rajat.inventoryservice.dto.InventoryResponse;
import com.rajat.inventoryservice.model.Inventory;
import com.rajat.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        Optional<List<Inventory>> optionalInventories =  inventoryRepository.findBySkuCodeIn(skuCode);
        
        return optionalInventories.map(inventories -> inventories.stream()
                .map(this::getInventoryResponse).toList()).orElse(null);
    }
    private InventoryResponse getInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }
}
