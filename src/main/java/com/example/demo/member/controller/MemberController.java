package com.example.demo.member.controller;

import com.example.demo.cart.entity.Cart;
import com.example.demo.cart.service.CartService;
import com.example.demo.cartItem.entity.CartItem;
import com.example.demo.cartItem.service.CartItemService;
import com.example.demo.mail.service.MailService;
import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.order.entity.Order;
import com.example.demo.order.service.OrderService;
import com.example.demo.orderItem.entity.OrderItem;
import com.example.demo.orderItem.service.OrderItemService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final CartService cartService;

    private final CartItemService cartItemService;

    private final OrderItemService orderItemService;

    private final OrderService orderService;

    private final MailService mailService;

    @GetMapping("/join_manual")
    public String joinManual() {
        return "member/join_manual";
    }

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

        if (!memberDto.getPassword().equals(memberDto.getPasswordCnf())) {
            return "member/join";
        }

        memberService.join(memberDto);

        return "redirect:/member/loginForm";
    }

    @GetMapping("/emailCheck")
    @ResponseBody
    public String emailCheck(@RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException {
        String authCode = mailService.sendSimpleMessage(email);
        return authCode;
    }

    @GetMapping("/loginForm")
    public String loginForm(@AuthenticationPrincipal Member member,
                            @AuthenticationPrincipal OAuth2User oauth2User) {

        if (member != null || oauth2User != null) {
            return "redirect:/";
        }

        return "member/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute MemberDto memberDto,
                        BindingResult bindingResult, Model model,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "로그인 실패! 아이디나 비밀번호를 확인하세요.");
            return "member/login";
        }

        try {

            Member member = memberService.login(memberDto.getUsername(), memberDto.getPassword());

            if (member == null) {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
                return "member/login";
            }

            if (member.isDeleted()) {
                model.addAttribute("error", "삭제된 계정입니다.");
                return "member/login";
            }

            List<GrantedAuthority> authorities = new ArrayList<>();

            authorities.add(new SimpleGrantedAuthority(member.getGrade().getValue()));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    member, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션에 인증 정보를 저장
            HttpSession session = request.getSession();
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            return "redirect:/";

        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생: ", e);
            model.addAttribute("error", "로그인 처리 중 오류가 발생했습니다.");
            return "member/login";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/mypage/me")
    public String meForm(@AuthenticationPrincipal Member member,
                         @AuthenticationPrincipal OAuth2User oAuth2User,
                         Model model) {

        if (member != null) {

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
        } else if (oAuth2User != null) {

            Member oAuth2member = memberService.findByProviderId(oAuth2User.getName());

            MemberDto memberDto = new MemberDto();
            memberDto.setUsername(oAuth2member.getUsername());
            memberDto.setName(oAuth2member.getName());
            memberDto.setNickname(oAuth2member.getNickname());
            memberDto.setEmail(oAuth2member.getEmail());
            memberDto.setPhone(oAuth2member.getPhone());
            memberDto.setAddr1(oAuth2member.getAddr1());
            memberDto.setAddr2(oAuth2member.getAddr2());

            model.addAttribute("member", oAuth2member);
            model.addAttribute("memberDto", memberDto);
        }


        return "member/me";
    }

    @GetMapping("/mypage/me/cart")
    public String cartList(@AuthenticationPrincipal Member member,
                           Model model) {

        Cart cart = cartService.findOrCreateCart(member);

        List<CartItem> cartItemList = cartItemService.findAllByCart(cart);

        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("cart", cart);

        return "member/cartItemList";
    }

    @GetMapping("/mypage/me/order")
    public String orderList(@AuthenticationPrincipal Member member,
                            Model model) {

        List<Order> orderList = orderService.findAllByMember(member);

        List<OrderItem> orderItemList = new ArrayList<>();

        for (Order order : orderList) {
            List<OrderItem> orderItems = orderItemService.findAllByOrder(order);
            orderItemList.addAll(orderItems);
        }

        model.addAttribute("orderItemList", orderItemList);

        model.addAttribute("orderList", orderList);

        return "member/orderList";
    }

    @PostMapping("/mypage/modify")
    public String modify(@AuthenticationPrincipal Member member,
                         @Valid @ModelAttribute MemberDto memberDto,
                         BindingResult bindingResult,
                         HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "member/me";
        }

        if (!memberDto.getPassword().equals(memberDto.getPasswordCnf())) {
            return "member/me";
        }

        if (!memberDto.getPassword().equals(memberDto.getPasswordCnf()) && (memberDto.getPassword().isEmpty() || memberDto.getPasswordCnf().isEmpty())) {
            return "member/me";
        }

        memberService.modify(member, memberDto);

        session.invalidate();

        return "redirect:/member/mypage/me";
    }

    @GetMapping("/mypage/delete")
    public String memberDelete(@AuthenticationPrincipal Member member) {

        memberService.delete(member);

        return "redirect:/member/logout";
    }

    @PostMapping("/idCheck")
    @ResponseBody
    public int idCheck(@RequestParam("username") String username) {

        Member member = memberService.findByUsername(username);

        return (member != null) ? 1 : 0;
    }
}
