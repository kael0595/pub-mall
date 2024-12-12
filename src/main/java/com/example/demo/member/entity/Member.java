package com.example.demo.member.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private String phone;

    @NotNull
    private String email;

    @NotNull
    private String nickname;

    @NotNull
    private String addr1;

    private String addr2;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Product> productList;

    private LocalDateTime updateDt;
}
