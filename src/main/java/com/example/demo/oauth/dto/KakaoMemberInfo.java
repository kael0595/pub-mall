package com.example.demo.oauth.dto;

import java.util.Map;

public class KakaoMemberInfo implements Oauth2MemberInfo {

    private Map<String, Object> attributes;

    private Map<String, Object> kakaoMemberAttributes;

    private Map<String, Object> profileAttributes;

    public KakaoMemberInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoMemberAttributes = (Map<String, Object>) attributes.get("kakao_account");
        this.profileAttributes = (Map<String, Object>) kakaoMemberAttributes.get("profile");
    }
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        return profileAttributes.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return kakaoMemberAttributes.get("email").toString();
    }
}
