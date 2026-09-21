package com.techie.order_service.controller;


import com.techie.order_service.entity.Order;
import com.techie.order_service.serviceImpl.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create Order
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(
            @RequestBody Order order) {

        Order savedOrder = orderService.createOrder(order);

        return new ResponseEntity<>(
                savedOrder,
                HttpStatus.CREATED
        );
    }

    // Get all Orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {

        List<Order> orders = orderService.getAllOrders();

        return ResponseEntity.ok(orders);
    }

    // Get Order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long id) {

        Order order = orderService.getOrderById(id);

        return ResponseEntity.ok(order);
    }
}
