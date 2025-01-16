package com.example.demo.comment.controller;

import com.example.demo.comment.dto.CommentDto;
import com.example.demo.comment.entity.Comment;
import com.example.demo.comment.service.CommentService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.oauth.dto.PrincipalDetails;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String addComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             @PathVariable("id") Long id,
                             @Valid CommentDto commentDto,
                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "product/detail";
        }

        Product product = productService.findById(id);

        if (principalDetails == null || principalDetails.getMember() == null) {
            throw new IllegalStateException("사용자를 찾을 수 없습니다.");
        }

        Member authticatedMember = principalDetails.getMember();

        log.info("authMember : {}", authticatedMember);

        commentService.addComment(product, commentDto.getContent(), authticatedMember);

        return "redirect:/product/detail/" + id;

    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@AuthenticationPrincipal Member member,
                                @AuthenticationPrincipal OAuth2User oauth2User,
                                @PathVariable("id") Long id) {

        Comment comment = commentService.findById(id);

        commentService.deleteComment(comment);

        return "redirect:/product/detail/" + id;
    }
}
