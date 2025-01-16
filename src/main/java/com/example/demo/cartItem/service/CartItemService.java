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
        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .price(product.getSalePrice())
                .amount(amount)
                .build();
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> findAllByCart(Cart cart) {
        return cartItemRepository.findAllByCart(cart);
    }

    public void deleteCartItem(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    public CartItem findByCartAndProduct(Cart cart, Product product) {
        return cartItemRepository.findByCartAndProduct(cart, product);
    }
}
