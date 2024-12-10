package com.example.demo.product.controller;

import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);
        return "product/list";
    }

    @GetMapping("/add")
    public String addForm(ProductDto productDto) {
        return "product/add";
    }

    @PostMapping("/add")
    public String add(@Valid ProductDto productDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "product/add";
        }

        Product product = productService.add(productDto);

        return "redirect:/product/list";

    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {

        Product product = productService.findById(id);
        model.addAttribute("product", product);

        productService.plusViewCount(product);

        return "product/detail";
    }

}
