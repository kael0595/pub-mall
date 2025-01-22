package com.example.demo.product.repository;

import com.example.demo.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAll(Specification<Product> specification, Pageable pageable);
}
