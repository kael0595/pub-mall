package com.example.demo.cart.controller;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.service.CartService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    private final ProductService productService;

    private final MemberService memberService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String viewCart(@AuthenticationPrincipal User user, Model model) {

        Member member = memberService.findByUsername(user.getUsername());

        Cart cart = cartService.findByMember(member);

        model.addAttribute("cart", cart);

        return "member/cartItemList";
    }

    @PostMapping("/add/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String addCartItem(@AuthenticationPrincipal User user,
                              @PathVariable("productId") Long productId,
                              @RequestParam("amount") int amount) {

        Member member = memberService.findByUsername(user.getUsername());

        Product product = productService.findById(productId);

        cartService.addCartItemToCart(product, member, amount);

        return "redirect:/member/mypage/me/cart";
    }



}
