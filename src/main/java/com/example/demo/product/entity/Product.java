package com.example.demo.product.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.cart.entity.Cart;
import com.example.demo.file.entity.FileUploadEntity;
import com.example.demo.member.entity.Member;
import com.example.demo.order.entity.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Product extends BaseEntity {

    @Column(nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(nullable = false, unique = true)
    @NotNull
    @NotBlank
    private String code;

    @Column(nullable = false)
    @NotNull
    private String category;

    @Column(nullable = false)
    @NotNull
    private String description;

    @Column(nullable = false)
    @NotNull
    private int standardPrice;

    @Column(nullable = false)
    @NotNull
    private int discount;

    private int salePrice;

    private int viewCount;

    private boolean deleted;

    private LocalDateTime updateDt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<FileUploadEntity> fileList;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    public void calculateSalePrice() {
       salePrice = this.standardPrice - (standardPrice * discount / 100);
    }


}
