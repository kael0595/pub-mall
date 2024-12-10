package com.example.demo.product.service;

import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product add(ProductDto productDto) {

        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setCode(productDto.getCode());
        product.setDiscount(productDto.getDiscount());
        product.setSkipAuditing(true);

        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void plusViewCount(Product product) {
        product.setSkipAuditing(true);
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
    }

    public void modify(Product product, ProductDto productDto) {

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setCode(productDto.getCode());
        product.setDiscount(productDto.getDiscount());
        productRepository.save(product);

    }

    public void delete(Product product) {
        product.setDeleted(true);
        productRepository.save(product);
    }
}
