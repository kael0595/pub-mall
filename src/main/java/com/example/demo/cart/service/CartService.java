package com.example.demo.cart.service;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.repository.CartRepository;
import com.example.demo.cartItem.entity.CartItem;
import com.example.demo.cartItem.service.CartItemService;
import com.example.demo.member.entity.Member;
import com.example.demo.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemService cartItemService;

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

        // 상품이 이미 장바구니에 있는지 확인
        Optional<CartItem> existingItemOpt = cart.getCartItemList().stream()
                .filter(item -> item.getProduct().equals(product)) // product를 기준으로 비교
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // 상품이 존재하면 수량만 증가
            CartItem existingItem = existingItemOpt.get();
            existingItem.setAmount(existingItem.getAmount() + amount);
        } else {
            // 상품이 없으면 새로 추가
            CartItem cartItem = cartItemService.createCartItem(product, cart, amount);
            cart.addCartItem(cartItem);
        }

        // 한 번만 저장
        cartRepository.save(cart);
    }

    public Cart findByMember(Member member) {
        return cartRepository.findByMember(member);
    }

}
