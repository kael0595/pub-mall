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
import java.util.Random;

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

        member.setNickname(memberDto.getNickname());
        member.setEmail(memberDto.getEmail());
        member.setPhone(memberDto.getPhone());
        member.setAddr1(memberDto.getAddr1());
        member.setAddr2(memberDto.getAddr2());
        member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
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

    public Member findByProviderId(String providerId) {
        return memberRepository.findByProviderId(providerId);
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    public Member findMemberByUsernameAndEmail(String username, String email) {
        return memberRepository.findMemberByUsernameAndEmail(username, email);
    }

    public String createTempPassword() {
        Random random = new Random();

        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97));
                case 1 -> key.append((char) (random.nextInt(26) + 65));
                case 2 -> key.append((char) (random.nextInt(10) + '0'));
            }
        }

        String tempPassword = key.toString();

        return tempPassword;
    }

    public String setTempPassword(Member member) {

        String tempPassword = createTempPassword();

        member.setPassword(passwordEncoder.encode(tempPassword));

        memberRepository.save(member);

        return tempPassword;

    }
}
