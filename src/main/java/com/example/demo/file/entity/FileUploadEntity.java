package com.example.demo.file.entity;

import com.example.demo.base.entity.BaseEntity;
import com.example.demo.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class FileUploadEntity extends BaseEntity {

    private String originalName;

    private String fileName;

    private String filePath;

    private String ext;

    private int size;

    private boolean deleted = false;

    private LocalDateTime deleteDt;

    private LocalDateTime updateDt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
