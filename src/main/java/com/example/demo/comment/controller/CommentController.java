package com.example.demo.comment.controller;

import com.example.demo.comment.dto.CommentDto;
import com.example.demo.comment.service.CommentService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    private final ProductService productService;

    private final MemberService memberService;

    @PostMapping("/add/{id}")
    public String addComment(@AuthenticationPrincipal Member member,
                             @AuthenticationPrincipal OAuth2User oauth2User,
                             @PathVariable("id") Long id,
                             @Valid CommentDto commentDto,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "product/detail";
        }

        Product product = productService.findById(id);

        log.info("id: {}", id);

        log.info("content: {}", commentDto.getContent());

        if (oauth2User != null) {
            Member oauthMember = memberService.findByProviderId(oauth2User.getAttribute("providerId"));
            commentService.addComment(product, commentDto.getContent(), oauthMember);
        } else {
            commentService.addComment(product, commentDto.getContent(), member);
        }

        return "redirect:/product/detail/" + id;

    }
}
