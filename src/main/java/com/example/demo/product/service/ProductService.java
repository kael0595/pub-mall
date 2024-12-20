package com.example.demo.product.service;

import com.example.demo.file.service.FileService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final FileService fileService;

    private final MemberRepository memberRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product add(User user, ProductDto productDto, MultipartFile[] files) throws IOException {

        Member member = memberRepository.findByUsername(user.getUsername());

        Product product = Product.builder()
                .name(productDto.getName())
                .standardPrice(productDto.getStandardPrice())
                .category(productDto.getCategory())
                .description(productDto.getDescription())
                .discount(productDto.getDiscount())
                .code(productDto.getCode())
                .member(member)
                .build();

        product.calculateSalePrice();

        productRepository.save(product);
        fileService.fileUpload(files, product);

        return product;
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void plusViewCount(Product product) {
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
    }

    public void modify(Member member, Product product, ProductDto productDto) {

        product.setName(productDto.getName());
        product.setStandardPrice(productDto.getStandardPrice());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setCode(productDto.getCode());
        product.setDiscount(productDto.getDiscount());
        product.calculateSalePrice();
        product.setUpdateDt(LocalDateTime.now());
        product.setMember(member);
        productRepository.save(product);


    }

    public void delete(Product product) {
        product.setDeleted(true);
        productRepository.save(product);
    }

    public boolean hasPermission(Product product, Member member) {
        return product.getMember().getUsername().equals(member.getUsername());
    }
}
