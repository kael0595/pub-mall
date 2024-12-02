package com.example.demo.base.member.entity;

import lombok.Getter;

@Getter
public enum Grade {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;

    Grade(String value) {
        this.value = value;
    }
}
