package com.example.demo.product.entity;

import com.example.demo.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PostLoad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

    private String name;

    private String code;

    private String category;

    private String description;

    private int price;
    
    private int discount;

    private int viewCount;

    private boolean deleted;

    @PostLoad
    public void resetAuditing() {
        setSkipAuditing(false);
    }

}
