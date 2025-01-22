package com.example.demo.main.controller;

import com.example.demo.notice.entity.Notice;
import com.example.demo.notice.service.NoticeService;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;

    private final NoticeService noticeService;

    @GetMapping("/")
    public String index(Model model) {

        List<Product> productList = productService.findAll();

        List<Notice> noticeList = noticeService.findAll();

        model.addAttribute("productList", productList);

        model.addAttribute("noticeList", noticeList);

        return "index";
    }
}
