package com.example.demo.notice.controller;

import com.example.demo.member.entity.Member;
import com.example.demo.notice.dto.NoticeDto;
import com.example.demo.notice.entity.Notice;
import com.example.demo.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/list")
    public String noticeList(Model model) {

        List<Notice> noticeList = noticeService.findAll();

        model.addAttribute("noticeList", noticeList);

        return "notice/list";
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
