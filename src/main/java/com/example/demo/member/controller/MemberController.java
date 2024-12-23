package com.example.demo.member.controller;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.service.CartService;
import com.example.demo.cartItem.entity.CartItem;
import com.example.demo.cartItem.service.CartItemService;
import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.order.entity.Order;
import com.example.demo.order.service.OrderService;
import com.example.demo.orderItem.entity.OrderItem;
import com.example.demo.orderItem.service.OrderItemService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final CartService cartService;

    private final CartItemService cartItemService;

    private final OrderItemService orderItemService;

    private final OrderService orderService;

    @GetMapping("/join")
    public String joinForm() {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberDto memberDto,
                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        if (!memberDto.getPassword1().equals(memberDto.getPassword2())) {
            return "member/join";
        }

        memberService.join(memberDto);

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@Valid MemberDto memberDto,
                        BindingResult bindingResult,
                        Model model) {

        if (bindingResult.hasErrors()) {
            return "member/login";
        }

        Member member = memberService.findByUsernameAndPassword(memberDto.getUsername(), memberDto.getPassword1());

        if (member == null) {
            model.addAttribute("member not found", "사용자를 찾을 수 없습니다.");
            return "member/login";
        }

        if (member.isDeleted()) {
            return "redirect:/member/login";
        }
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/mypage/me")
    public String meForm(@AuthenticationPrincipal User user,
                         Model model) {

        Member member = memberService.findByUsername(user.getUsername());

        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(member.getUsername());
        memberDto.setName(member.getName());
        memberDto.setNickname(member.getNickname());
        memberDto.setEmail(member.getEmail());
        memberDto.setPhone(member.getPhone());
        memberDto.setAddr1(member.getAddr1());
        memberDto.setAddr2(member.getAddr2());

        model.addAttribute("member", member);
        model.addAttribute("memberDto", memberDto);

        return "member/me";
    }

    @GetMapping("/mypage/me/cart")
    public String cartList(@AuthenticationPrincipal User user,
                       Model model) {
        Member member = memberService.findByUsername(user.getUsername());

        Cart cart = cartService.findOrCreateCart(member);

        List<CartItem> cartItemList = cartItemService.findAllByCart(cart);

        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("cart", cart);

        return "member/cartItemList";
    }

    @GetMapping("/mypage/me/order")
    public String orderList(@AuthenticationPrincipal User user,
                            Model model) {
        Member member = memberService.findByUsername(user.getUsername());

        List<Order> orderList = orderService.findAllByMember(member);

        List<OrderItem> orderItemList = new ArrayList<>();

        for (Order order : orderList) {
            List<OrderItem> orderItems = orderItemService.findAllByOrder(order);
            orderItemList.addAll(orderItems);
        }

        System.out.println(orderItemList.size());

        model.addAttribute("orderItemList", orderItemList);

        model.addAttribute("orderList", orderList);

        return "member/orderList";
    }

    @PostMapping("/mypage/modify")
    public String modify(@AuthenticationPrincipal User user,
                         @Valid @ModelAttribute MemberDto memberDto,
                         BindingResult bindingResult,
                         HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "member/me";
        }

        if (!memberDto.getPassword1().equals(memberDto.getPassword2())) {
            return "member/me";
        }

        Member member = memberService.findByUsername(user.getUsername());

        if (!memberDto.getPassword1().equals(memberDto.getPassword2()) && (memberDto.getPassword1().isEmpty() || memberDto.getPassword2().isEmpty())) {
            return "member/me";
        }

        memberService.modify(member, memberDto);

        session.invalidate();

        return "redirect:/member/mypage/me";
    }

    @GetMapping("/mypage/delete")
    public String memberDelete (@AuthenticationPrincipal User user) {

        Member member = memberService.findByUsername(user.getUsername());

        memberService.delete(member);

        return "redirect:/member/logout";
    }

    @PostMapping("/idCheck")
    @ResponseBody
    public int idCheck(@RequestParam("username") String username){

        Member member = memberService.findByUsername(username);

        return (member != null) ? 1 : 0;
    }

}
