package com.example.demo.member.controller;

import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @GetMapping("/mypage/me")
    public String meForm(@AuthenticationPrincipal User user,
                         Model model) {

        System.out.println(user.getUsername());

        Member member = memberService.findByUsername(user.getUsername());

        System.out.println(member.getUsername());

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

        memberService.modify(member, memberDto);

        session.invalidate();

        return "redirect:/member/mypage/me";
    }
}
