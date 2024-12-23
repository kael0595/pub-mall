package com.example.demo.orderItem.service;

import com.example.demo.order.entity.Order;
import com.example.demo.orderItem.entity.OrderItem;
import com.example.demo.orderItem.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public List<OrderItem> findAllByOrder(Order order) {
        return orderItemRepository.findAllByOrder(order);
    }
}
