package com.example.demo.admin.controller;

import com.example.demo.admin.service.AdminService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.notice.dto.NoticeDto;
import com.example.demo.notice.entity.Notice;
import com.example.demo.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final MemberService memberService;

    private final NoticeService noticeService;

    @GetMapping("/memberList")
    public String memberList(Model model) {

        List<Member> memberList = memberService.getAll();
        model.addAttribute("memberList", memberList);
        return "admin/memberList";

    }

    @GetMapping("/memberDelete/{id}")
    public String memberDelete(@PathVariable("id") Long id) {
        Member member = memberService.findById(id);
        adminService.deleteMember(member);
        return "redirect:/admin/memberList";
    }

    @GetMapping("/notice/add")
    public String noticeAddForm(NoticeDto noticeDto, Model model) {
        model.addAttribute("noticeDto", noticeDto);
        return "notice/add";
    }

    @PostMapping("/notice/add")
    public String noticeAdd(@AuthenticationPrincipal Member member,
                            @Valid @ModelAttribute NoticeDto noticeDto,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "notice/add";
        }

        Notice notice = noticeService.add(noticeDto, member);
        return "redirect:/notice/list";
    }

}
