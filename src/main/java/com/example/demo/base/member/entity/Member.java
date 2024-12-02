package com.example.demo.base.member.entity;

import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EntityListeners(AuditingEntityListener.class)
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
