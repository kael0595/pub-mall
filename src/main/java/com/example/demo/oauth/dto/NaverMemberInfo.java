package com.example.demo.oauth.dto;

import java.util.Map;

public class NaverMemberInfo implements Oauth2MemberInfo {
    public NaverMemberInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.response = (Map<String, Object>) attributes.get("response");
    }

    private Map<String, Object> attributes;

    private Map<String, Object> response;

    @Override
    public String getProviderId() {
        return (String) response.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}
