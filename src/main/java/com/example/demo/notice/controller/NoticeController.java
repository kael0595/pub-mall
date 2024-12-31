package com.example.demo.notice.controller;

import com.example.demo.notice.entity.Notice;
import com.example.demo.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        return "notice/list";
    }
}
