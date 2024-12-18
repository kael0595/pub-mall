package com.example.demo.cart.service;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.cartItem.entity.CartItem;
import com.example.demo.cartItem.service.CartItemService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemService cartItemService;

    private final MemberRepository memberRepository;

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Cart findOrCreateCart(Member member) {

        Cart cart = cartRepository.findByMember(member);

        if (cart == null) {
            cart = new Cart();
            cart.setMember(member);
            cartRepository.save(cart);
        }
        return cart;
    }

    @Transactional
    public void addCartItemToCart(Product product, Member member, int amount) {
        Cart cart = findOrCreateCart(member);
        CartItem cartItem = cartItemService.createCartItem(product, cart, amount);
        cart.addCartItem(cartItem);
        cartRepository.save(cart);
    }
}
