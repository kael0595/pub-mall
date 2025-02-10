package com.example.demo.admin.controller;

import com.example.demo.admin.service.AdminService;
import com.example.demo.cashLog.entity.CashLog;
import com.example.demo.cashLog.service.CashLogService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private final MemberService memberService;

    private final CashLogService cashLogService;

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

    @GetMapping("/cal")
    public String cal(Model model) {
        List<CashLog> cashLogList = cashLogService.findAll();
        model.addAttribute("cashLogList", cashLogList);
        return "admin/cal";
    }

    @GetMapping("/cal/custom")
    public ResponseEntity<BigDecimal> calCustom(@RequestParam("start") String start,
                                                @RequestParam("end") String end) {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);


        LocalDateTime startDt = startDate.atStartOfDay();
        LocalDateTime endDt = endDate.atTime(23, 59, 59);

        BigDecimal revenue = adminService.calculateRevenueBetween(startDt, endDt);
        return ResponseEntity.ok(revenue);
    }

}
