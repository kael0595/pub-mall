package com.example.demo.oauth.service;

import com.example.demo.member.entity.Grade;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.oauth.dto.GoogleMemberInfo;
import com.example.demo.oauth.dto.KakaoMemberInfo;
import com.example.demo.oauth.dto.NaverMemberInfo;
import com.example.demo.oauth.dto.Oauth2MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("OAuth2 요청 처리 시작 - Provider: {}", userRequest.getClientRegistration().getRegistrationId());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Oauth2MemberInfo memberInfo = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String principalKey = "";

        if (registrationId.equals("kakao")) {
            memberInfo = new KakaoMemberInfo(oAuth2User.getAttributes());
            principalKey = "id";
        } else if (registrationId.equals("naver")) {
            memberInfo = new NaverMemberInfo(oAuth2User.getAttributes());
            principalKey = "response";
        } else if (registrationId.equals("google")) {
            memberInfo = new GoogleMemberInfo(oAuth2User.getAttributes());
            principalKey = "sub";
        } else {
            throw new OAuth2AuthenticationException("지원되지 않는 OAuth2 Provider입니다.");
        }

        String provider = memberInfo.getProvider();

        String providerId = memberInfo.getProviderId();

        String oauth2Id = provider + "_" + providerId;

        System.out.println(oAuth2User.getAttributes());

        String name = memberInfo.getName();

        String email = "";

        if (memberInfo.getEmail() == null || memberInfo.getEmail().isEmpty()) {
            log.warn("카카오 로그인 사용자가 이메일 제공에 동의하지 않았습니다.");
            email = "no-email@provider.com"; // 기본 이메일 값 설정
        } else {
            email = memberInfo.getEmail();
        }

        Member member = memberRepository.findByOauth2Id(oauth2Id)
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .username(oauth2Id)
                            .oauth2Id(oauth2Id)
                            .name(name)
//                            .email(email)  // 기본 이메일 설정
                            .grade(Grade.BRONZE)
                            .provider(provider)
                            .providerId(providerId)
                            .build();
                    return memberRepository.save(newMember);
                });

        if (!oAuth2User.getAttributes().containsKey(principalKey)) {
            throw new OAuth2AuthenticationException("Principal key not found." + principalKey);
        }
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), oAuth2User.getAttributes(), principalKey);
    }
}
