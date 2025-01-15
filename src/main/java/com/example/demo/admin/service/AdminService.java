package com.example.demo.admin.service;

import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public void deleteMember(Member member) {

        if (!member.isDeleted()) {
            member.setDeleted(true);
        } else {
            member.setDeleted(false);
        }

        memberRepository.save(member);
    }
}
