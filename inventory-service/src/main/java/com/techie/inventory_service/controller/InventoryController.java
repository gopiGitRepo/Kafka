package com.techie.inventory_service.controller;


import com.techie.inventory_service.entity.Inventory;
import com.techie.inventory_service.requestResponse.InventoryRequest;
import com.techie.inventory_service.serviceImpl.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService) {

        this.inventoryService = inventoryService;
    }

    // Create Inventory
    @PostMapping
    public ResponseEntity<Inventory> createInventory(
            @RequestBody InventoryRequest inventory) {

        Inventory savedInventory =
                inventoryService.createInventory(inventory);

        return ResponseEntity.ok(savedInventory);
    }

    // Get all inventory
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {

        List<Inventory> inventory =
                inventoryService.getAllInventory();

        return ResponseEntity.ok(inventory);
    }

    // Get inventory by ID
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(
            @PathVariable Long id) {

        Inventory inventory =
                inventoryService.getInventoryById(id);

        return ResponseEntity.ok(inventory);
    }
}
