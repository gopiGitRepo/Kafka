package com.techie.order_service.serviceImpl;

import com.techie.order_service.entity.Order;
import com.techie.order_service.repo.OrderRepository;
import com.techie.order_service.requestResponse.OrderRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderRequest order) {
        Order newOrder = new Order();
        newOrder.setProductId(order.getProductId());
        newOrder.setQuantity(order.getQuantity());
        return orderRepository.save(newOrder);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found with id: " + id
                        )
                );
    }
}
