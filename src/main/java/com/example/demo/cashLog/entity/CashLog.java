package com.example.demo.cashLog.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.member.entity.Member;
import com.example.demo.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CashLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String productName;

    private String customerName;

    private int price;
}
