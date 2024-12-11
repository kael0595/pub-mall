package com.example.demo.product.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.file.entity.FileUploadEntity;
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
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

    @Column(nullable = false, unique = true)
    @NotNull
    private String name;

    @Column(nullable = false, unique = true)
    @NotNull
    private String code;

    @Column(nullable = false)
    @NotNull
    private String category;

    @Column(nullable = false)
    @NotNull
    private String description;

    @Column(nullable = false)
    @NotNull
    private int price;

    private int discount;

    private int viewCount;

    private boolean deleted;

    private LocalDateTime updateDt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<FileUploadEntity> fileList;

}
