package com.example.demo.admin.service;

import com.example.demo.cashLog.repository.CashLogRepository;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    private final CashLogRepository cashLogRepository;

    public void deleteMember(Member member) {

        if (!member.isDeleted()) {
            member.setDeleted(true);
        } else {
            member.setDeleted(false);
        }

        memberRepository.save(member);
    }

    public BigDecimal calculateRevenueBetween(LocalDateTime start, LocalDateTime end) {
        return cashLogRepository.getTotalRevenue(start, end);
    }
}
