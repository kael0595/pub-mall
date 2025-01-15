package com.example.demo.comment.service;

import com.example.demo.comment.entity.Comment;
import com.example.demo.comment.repository.CommentRepository;
import com.example.demo.member.entity.Member;
import com.example.demo.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void addComment(Product product, String content, Member member) {
        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .product(product)
                .build();
        commentRepository.save(comment);
    }
}
