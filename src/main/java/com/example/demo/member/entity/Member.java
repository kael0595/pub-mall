package com.example.demo.member.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.cart.entity.Cart;
import com.example.demo.cashLog.entity.CashLog;
import com.example.demo.order.entity.Order;
import com.example.demo.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    @Column(unique = true)
    private String username;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String phone;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String nickname;

    @NotNull
    @NotEmpty
    private String addr1;

    private String addr2;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private boolean deleted;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Product> productList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Order> orderList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<CashLog> cashLogList;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private LocalDateTime updateDt;
}
