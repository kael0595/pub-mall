package com.example.demo.base.member.service;

import com.example.demo.base.member.dto.MemberDto;
import com.example.demo.base.member.entity.Grade;
import com.example.demo.base.member.entity.Member;
import com.example.demo.base.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public void join(MemberDto memberDto) {


        Member member = Member.builder()
                .username(memberDto.getUsername())
                .name(memberDto.getName())
                .password(passwordEncoder.encode(memberDto.getPassword1()))
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .phone(memberDto.getPhone())
                .addr1(memberDto.getAddr1())
                .addr2(memberDto.getAddr2())
                .build();

        if (member.getUsername().startsWith("admin")) {
            member.setGrade(Grade.ADMIN);
        } else {
            member.setGrade(Grade.USER);
        }

        memberRepository.save(member);
    }
}