package com.example.demo.member.service;

import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.entity.Grade;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
            member.setGrade(Grade.BRONZE);
        }

        memberRepository.save(member);
    }

    public Member findByUsernameAndPassword(String username, String password) {
        return memberRepository.findByUsernameAndPassword(username, password);
    }

    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    public Member findByUsername(String name) {
        return memberRepository.findByUsername(name);
    }

    public void modify(Member member, MemberDto memberDto) {
        member.setNickname(memberDto.getNickname());
        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
        member.setName(memberDto.getName());
        member.setAddr1(memberDto.getAddr1());
        member.setAddr2(memberDto.getAddr2());
        member.setPassword(passwordEncoder.encode(memberDto.getPassword1()));
        member.setUpdateDt(LocalDateTime.now());
        memberRepository.save(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public void delete(Member member) {
        member.setDeleted(true);
        member.setUpdateDt(LocalDateTime.now());
        memberRepository.save(member);
    }
}
