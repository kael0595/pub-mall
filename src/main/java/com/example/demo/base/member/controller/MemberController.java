package com.example.demo.base.member.controller;

import com.example.demo.base.member.dto.MemberDto;
import com.example.demo.base.member.entity.Member;
import com.example.demo.base.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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

        return "redirect:/";
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
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
