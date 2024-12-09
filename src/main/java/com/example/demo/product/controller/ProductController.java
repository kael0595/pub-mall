package com.example.demo.product.controller;

import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String addForm(ProductDto productDto,
                          Model model) {
        model.addAttribute("product", productDto);
        return "product/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute ProductDto productDto, BindingResult bindingResult) {

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

    @GetMapping("/modify/{id}")
    public String modifyForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product/modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id,
                         @Valid @ModelAttribute ProductDto productDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "product/modify";
        }

        Product product = productService.findById(id);

        productService.modify(product, productDto);


        return "redirect:/product/detail/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        Product product = productService.findById(id);

        productService.delete(product);
        return "redirect:/product/list";
    }

}
