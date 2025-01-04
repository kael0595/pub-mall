package com.example.demo.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

    private String username;

    private String name;

    private String password;

    private String passwordCnf;

    private String phone;

    private String email;

    private String nickname;

    private String addr1;

    private String addr2;
}
