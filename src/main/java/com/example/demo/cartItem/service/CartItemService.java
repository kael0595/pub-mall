package com.example.demo.cartItem.service;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cartItem.entity.CartItem;
import com.example.demo.cartItem.repository.CartItemRepository;
import com.example.demo.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItem createCartItem(Product product, Cart cart, int amount) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setAmount(amount);
        cartItem.setCart(cart);
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> findAll() {
        return cartItemRepository.findAll();
    }
}
