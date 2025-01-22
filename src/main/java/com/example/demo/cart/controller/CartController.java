package com.example.demo.cart.controller;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.service.CartService;
import com.example.demo.cartItem.entity.CartItem;
import com.example.demo.cartItem.service.CartItemService;
import com.example.demo.member.entity.Member;
import com.example.demo.oauth.dto.PrincipalDetails;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
@Slf4j
public class CartController {

    private final CartService cartService;

    private final ProductService productService;

    private final CartItemService cartItemService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String viewCart(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        Member member = principalDetails.getMember();

        Cart cart = cartService.findByMember(member);

        model.addAttribute("cart", cart);

        return "member/cartItemList";
    }

    @PostMapping("/add/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String addCartItem(@AuthenticationPrincipal PrincipalDetails principalDetails,
                              @PathVariable("productId") Long productId,
                              @RequestParam("amount") int amount) {

        Member member = principalDetails.getMember();

        Product product = productService.findById(productId);

        cartService.addCartItemToCart(product, member, amount);

        return "redirect:/member/mypage/me/cart";
    }

    @PostMapping("/deleteCartItem")
    public String deleteCartItem(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                 @RequestParam("id") Long id,
                                 @RequestParam("amount") int amount) {

        if (id == null || amount <= 0 || id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 입력값입니다.");
        }

        Member member = principalDetails.getMember();

        Cart cart = cartService.findByMember(member);

        CartItem cartItem = cartItemService.findCartItemByCartAndProduct(cart, id);

        if (cartItem == null) {
            throw new AccessDeniedException("해당 상품은 장바구니에 존재하지 않습니다.");
        }

        if (cartItem.getAmount() < amount) {
            throw new AccessDeniedException("삭제하려는 상품의 수량이 장바구니에 있는 상품의 수량보다 많습니다.");
        }

        cartItem.setAmount(cartItem.getAmount() - amount);

        if (cartItem.getAmount() == 0) {
            cartItemService.deleteCartItem(cartItem);
        } else {
            cartItemService.save(cartItem);
        }

        return "redirect:/member/mypage/me/cart";
    }


}
