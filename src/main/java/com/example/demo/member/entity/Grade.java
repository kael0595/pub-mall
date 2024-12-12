package com.example.demo.member.entity;

import lombok.Getter;

@Getter
public enum Grade {

    ADMIN("ROLE_ADMIN"),
    BRONZE("ROLE_BRONZE"),
    SILVER("ROLE_SILVER"),
    GOLD("ROLE_GOLD");

    private final String value;

    Grade(String value) {
        this.value = value;
    }
}
