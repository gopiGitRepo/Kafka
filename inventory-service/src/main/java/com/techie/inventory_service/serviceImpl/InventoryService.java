package com.techie.inventory_service.serviceImpl;


import com.techie.inventory_service.entity.Inventory;
import com.techie.inventory_service.repo.InventoryRepository;
import com.techie.inventory_service.requestResponse.InventoryRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(
            InventoryRepository inventoryRepository) {

        this.inventoryRepository = inventoryRepository;
    }

    public Inventory createInventory(InventoryRequest inventory) {
        Inventory newInventory = new Inventory();
        newInventory.setProductId(inventory.getProductId());
        newInventory.setAvailableQuantity(inventory.getAvailableQuantity());
        return inventoryRepository.save(newInventory);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory getInventoryById(Long id) {

        return inventoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found with id: " + id
                        )
                );
    }
}
