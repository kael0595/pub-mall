package com.example.demo.cartItem.repository;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cartItem.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCart(Cart cart);

}
