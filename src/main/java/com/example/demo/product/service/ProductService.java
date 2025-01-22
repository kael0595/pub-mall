package com.example.demo.product.service;

import com.example.demo.comment.entity.Comment;
import com.example.demo.file.service.FileService;
import com.example.demo.member.entity.Member;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final FileService fileService;

    private Specification<Product> search(String keyword) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            public Predicate toPredicate(Root<Product> p, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<Product, Member> u1 = p.join("member", JoinType.LEFT);
                Join<Product, Comment> a = p.join("commentList", JoinType.LEFT);
                Join<Comment, Member> u2 = p.join("member", JoinType.LEFT);
                return cb.or(cb.like(p.get("name"), "%" + keyword + "%"), // 제목
                        cb.like(p.get("category"), "%" + keyword + "%"),      // 내용
                        cb.like(p.get("description"), "%" + keyword + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + keyword + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + keyword + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + keyword + "%"));   // 답변 작성자
            }
        };
    }

    public Page<Product> findAll(int page, String keyword) {

        List<Sort.Order> sorts = new ArrayList<>();

        sorts.add(Sort.Order.desc("createDt"));

        Specification<Product> specification = search(keyword);

        Pageable pageable = PageRequest.of(page, 10);

        return productRepository.findAll(specification, pageable);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public Product add(Member member, ProductDto productDto, MultipartFile[] files) throws IOException {

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
        product.setUpdateDt(LocalDateTime.now());
        product.setMember(member);

        product.calculateSalePrice();

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
