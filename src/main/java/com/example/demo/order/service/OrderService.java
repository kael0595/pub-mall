package com.example.demo.order.service;

import com.example.demo.cart.repository.CartRepository;
import com.example.demo.cartItem.repository.CartItemRepository;
import com.example.demo.cashLog.entity.CashLog;
import com.example.demo.cashLog.entity.CashLogStatus;
import com.example.demo.cashLog.repository.CashLogRepository;
import com.example.demo.member.entity.Member;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.orderItem.entity.OrderItem;
import com.example.demo.orderItem.repository.OrderItemRepository;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final OrderItemRepository orderItemRepository;

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final CashLogRepository cashLogRepository;

    @Transactional
    public Order createOrder(Member member, Long productId, int amount) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        int totalPrice = product.getSalePrice() * amount;

        Order order = Order.builder()
                .member(member)
                .status(OrderStatus.PENDING)
                .totalPrice(totalPrice)
                .shippingAddress(member.getAddr1() + " " + member.getAddr2())
                .build();
        orderRepository.save(order);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .amount(amount)
                .price(totalPrice)
                .build();
        orderItemRepository.save(orderItem);

        CashLog cashLog = CashLog.builder()
                .order(order)
                .price(totalPrice)
                .status(CashLogStatus.PENDING)
                .build();
        cashLogRepository.save(cashLog);

        return order;
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    public List<Order> findAllByMember(Member member) {
        return orderRepository.findAllByMember(member);
    }
}
