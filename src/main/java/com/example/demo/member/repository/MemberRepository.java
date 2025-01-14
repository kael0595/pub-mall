package com.example.demo.member.repository;

import com.example.demo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);

    Optional<Member> findByOauth2Id(String oauth2Id);

    Member findByProviderId(String providerId);

    Member findMemberByEmail(String email);

    Member findMemberByUsernameAndEmail(String username, String email);
}
