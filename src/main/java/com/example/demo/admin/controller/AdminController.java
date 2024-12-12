package com.example.demo.admin.controller;

import com.example.demo.admin.service.AdminService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final MemberService memberService;

    @GetMapping("/memberList")
    public String memberList(Model model) {

        List<Member> memberList = memberService.getAll();
        model.addAttribute("memberList", memberList);
        return "admin/memberList";

    }
}
