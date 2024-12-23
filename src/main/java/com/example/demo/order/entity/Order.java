package com.example.demo.order.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.cartItem.entity.CartItem;
import com.example.demo.member.entity.Member;
import com.example.demo.orderItem.entity.OrderItem;
import com.example.demo.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder(toBuilder = true)
@Table(name = "product_order")
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<OrderItem> orderItemList;

    private int amount;

    private int totalPrice;

    private OrderStatus status;

    private String shippingAddress;

    private String paymentMethod;

}
