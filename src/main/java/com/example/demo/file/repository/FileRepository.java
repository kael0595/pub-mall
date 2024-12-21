package com.example.demo.file.repository;

import com.example.demo.file.entity.FileUploadEntity;
import com.example.demo.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileUploadEntity, Long> {
    FileUploadEntity findByProduct(Product product);

    List<FileUploadEntity> findAllByProduct(Product product);
}
