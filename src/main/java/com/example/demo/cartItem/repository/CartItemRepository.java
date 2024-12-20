package com.example.demo.cartItem.repository;

import com.example.demo.cartItem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
