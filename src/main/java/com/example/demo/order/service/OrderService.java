package com.example.demo.order.service;

import com.example.demo.cashLog.entity.CashLog;
import com.example.demo.cashLog.entity.CashLogStatus;
import com.example.demo.cashLog.repository.CashLogRepository;
import com.example.demo.mail.service.MailService;
import com.example.demo.member.entity.Grade;
import com.example.demo.member.entity.Member;
import com.example.demo.order.entity.Order;
import com.example.demo.order.entity.OrderStatus;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.orderItem.entity.OrderItem;
import com.example.demo.orderItem.repository.OrderItemRepository;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import jakarta.mail.MessagingException;
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

    private final CashLogRepository cashLogRepository;

    private final MailService mailService;

    @Transactional
    public Order createOrder(Member member, Long productId, int amount) throws MessagingException {

        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        if (amount < 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        int discount = member.getDiscountAmount();

        int totalPrice = product.getSalePrice() * amount;

        totalPrice -= totalPrice * discount / 100;

        Order order = Order.builder()
                .member(member)
                .status(OrderStatus.PENDING)
                .totalPrice(totalPrice)
                .product(product)
                .amount(amount)
                .shippingAddress(member.getAddr1() + " " + member.getAddr2())
                .build();
        orderRepository.save(order);

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .amount(amount)
                .price(product.getSalePrice())
                .build();
        orderItemRepository.save(orderItem);

        CashLog cashLog = CashLog.builder()
                .order(order)
                .price(totalPrice)
                .member(member)
                .status(CashLogStatus.PENDING)
                .build();
        cashLogRepository.save(cashLog);

        member.setTotalAmount(member.getTotalAmount() + totalPrice);

        if (member.getTotalAmount() < 200000) {
            member.setGrade(Grade.BRONZE);
        } else if (member.getTotalAmount() < 500000) {
            member.setGrade(Grade.SILVER);
        } else if (member.getTotalAmount() < 1000000) {
            member.setGrade(Grade.GOLD);
        }

        if (amount > 1) {
            mailService.sendProductCodes(member.getEmail(), amount);
        } else {
            mailService.sendProductCode(member.getEmail());
        }

        return order;
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    public List<Order> findAllByMember(Member member) {
        return orderRepository.findAllByMember(member);
    }

}
