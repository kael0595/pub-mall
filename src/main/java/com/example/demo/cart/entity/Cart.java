package com.example.demo.cart.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.cartItem.entity.CartItem;
import com.example.demo.member.entity.Member;
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
@SuperBuilder
public class Cart extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CartItem> cartItemList = new ArrayList<>();

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItemList.add(cartItem);
        cartItem.setCart(this);
    }
}
