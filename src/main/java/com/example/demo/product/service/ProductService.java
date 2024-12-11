package com.example.demo.product.service;

import com.example.demo.file.service.FileService;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final FileService fileService;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product add(ProductDto productDto, MultipartFile[] files)  throws IOException {

        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setCode(productDto.getCode());
        product.setDiscount(productDto.getDiscount());

        fileService.fileUpload(files, product.getId());

        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void plusViewCount(Product product) {
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
