package com.example.demo.oauth.service;

import com.example.demo.member.entity.Grade;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.oauth.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        String principalKey = getPrincipalKey(provider);

        String uniqueId = provider + "_" + oAuth2User.getAttribute(principalKey);

        String providerId = oAuth2User.getAttribute(principalKey).toString();

        String email = oAuth2User.getAttribute("email");

        if (email == null) {
            email = uniqueId;
        }

        Member member = memberRepository.findByOauth2Id(uniqueId).orElseGet(
                () -> {
                    Member newMember = Member.builder()
                            .username(uniqueId)
                            .oauth2Id(uniqueId)
                            .name(uniqueId)
                            .isSocialLogin(true)
                            .grade(Grade.BRONZE)
                            .provider(provider)
                            .providerId(providerId)
                            .build();
                    return memberRepository.save(newMember);
                });

        List<GrantedAuthority> auth = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + member.getGrade().name()));
        return new PrincipalDetails(member, oAuth2User.getAttributes(), auth);
    }

    ;

//        if (provider.equals("kakao")) {
//            Member member = memberRepository.findByOauth2Id(oauth2Id)
//                    .orElseGet(() -> {
//                        Member newMember = Member.builder()
//                                .username(oauth2Id)
//                                .oauth2Id(oauth2Id)
//                                .name(name)
//                                .isSocialLogin(true)
//                                .grade(Grade.BRONZE)
//                                .provider(provider)
//                                .providerId(providerId)
//                                .build();
//                        return memberRepository.save(newMember);
//                    });
//        } else {
//
//            String email = memberInfo.getEmail();
//
//            Member member = memberRepository.findByOauth2Id(oauth2Id)
//                    .orElseGet(() -> {
//                        Member newMember = Member.builder()
//                                .username(oauth2Id)
//                                .oauth2Id(oauth2Id)
//                                .name(name)
//                                .isSocialLogin(true)
//                                .email(email)  // 기본 이메일 설정
//                                .grade(Grade.BRONZE)
//                                .provider(provider)
//                                .providerId(providerId)
//                                .build();
//                        return memberRepository.save(newMember);
//                    });
//        }
//
//        if (!oAuth2User.getAttributes().containsKey(principalKey)) {
//            throw new OAuth2AuthenticationException("Principal key not found." + principalKey);
//        }
//        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), oAuth2User.getAttributes(), principalKey);


    private String getPrincipalKey(String provider) {
        switch (provider) {
            case "kakao":
                return "id";
            case "naver":
                return "response";
            case "google":
                return "sub";
            default:
                throw new OAuth2AuthenticationException(new OAuth2Error("unsupported provider: " + provider), "지원하지 않는 provider 입니다.");
        }
    }
}
