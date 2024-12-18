package com.example.demo.cart.repository;

import com.example.demo.cart.entity.Cart;
import com.example.demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMember(Member member);
}
