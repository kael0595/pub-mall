package com.example.demo.member.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.cart.entity.Cart;
import com.example.demo.cashLog.entity.CashLog;
import com.example.demo.comment.entity.Comment;
import com.example.demo.notice.entity.Notice;
import com.example.demo.order.entity.Order;
import com.example.demo.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {

    //    @NotNull
//    @NotEmpty
//    @Column(unique = true)
    private String username;

    //    @NotNull
//    @NotEmpty
    private String name;

    //    @NotNull
//    @NotEmpty
    private String password;

    //    @NotNull
//    @NotEmpty
    private String phone;

    //    @NotNull
//    @NotEmpty
    private String email;

    //    @NotNull
//    @NotEmpty
    private String nickname;

    private String addr1;

    private String addr2;

    private String provider;

    private String providerId;

    private String oauth2Id;

    private int totalAmount;

    private int discountAmount;

    private boolean isSocialLogin = false;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private boolean deleted;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Product> productList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Order> orderList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CashLog> cashLogList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Notice> noticeList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Comment> commentList;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private LocalDateTime updateDt;

    public Member(String oauth2Id, String name, String password, String email, Grade grade, String provider, String providerId) {

        this.oauth2Id = oauth2Id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.grade = grade;
        this.provider = provider;
        this.providerId = providerId;

    }

    public int getDiscountAmount() {
        switch (this.grade) {
            case SILVER:
                return 5;
            case GOLD:
                return 10;
            default:
                return 0;
        }
    }

}
