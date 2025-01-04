package com.example.demo.member.service;

import com.example.demo.member.dto.MemberDto;
import com.example.demo.member.entity.Grade;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public void join(MemberDto memberDto) {

        Member member = Member.builder()
                .username(memberDto.getUsername())
                .name(memberDto.getName())
                .password(passwordEncoder.encode(memberDto.getPassword()))
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

    public Member login(String username, String password) {

        Member member = memberRepository.findByUsername(username);

        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            return member;
        } else {
            return null;
        }
    }

    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    public Member findByUsername(String name) {
        return memberRepository.findByUsername(name);
    }

    public void modify(Member member, MemberDto memberDto) {

        member.toBuilder()
                .nickname(memberDto.getNickname())
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .phone(memberDto.getPhone())
                .addr1(memberDto.getAddr1())
                .addr2(memberDto.getAddr2())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .updateDt(LocalDateTime.now());

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

    public Member findByProviderId(String providerId) {
        return memberRepository.findByProviderId(providerId);
    }
}
