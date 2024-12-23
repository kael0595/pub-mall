package com.example.demo.orderItem.repository;

import com.example.demo.order.entity.Order;
import com.example.demo.orderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrder(Order order);
}
