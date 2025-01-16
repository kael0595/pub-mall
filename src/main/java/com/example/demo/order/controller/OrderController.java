package com.example.demo.order.controller;

import com.example.demo.cart.service.CartService;
import com.example.demo.cartItem.service.CartItemService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.oauth.dto.PrincipalDetails;
import com.example.demo.order.entity.Order;
import com.example.demo.order.service.OrderService;
import com.example.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    private final ProductService productService;

    private final MemberService memberService;

    private final CartService cartService;

    private final CartItemService cartItemService;

    @GetMapping("/confirmation/{id}")
    public String orderConfirmation(@PathVariable("id") Long id, Model model) {

        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order/confirmation";

    }

    @PostMapping("/create")
    public String orderCreate(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         @RequestParam("productId") Long productId,
                         @RequestParam("amount") int amount) {

        Member member = principalDetails.getMember();

        Order order = orderService.createOrder(member, productId, amount);

        Long orderId = order.getId();

        return "redirect:/order/confirmation/" + orderId;
    }


}
