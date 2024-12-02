package com.example.demo.base.member.entity;

import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Member extends BaseEntity {

    private String userId;

    private String username;

    private String password;

    private String phone;

    private String email;

    private String nickname;

    private String addr1;

    private String addr2;
}
