package com.example.demo.order.repository;

import com.example.demo.member.entity.Member;
import com.example.demo.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByMember(Member member);
}
