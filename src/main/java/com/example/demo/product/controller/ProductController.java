package com.example.demo.product.controller;

import com.example.demo.file.entity.FileUploadEntity;
import com.example.demo.file.service.FileService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    private final FileService fileService;

    private final MemberService memberService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);
        return "product/list";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addForm(ProductDto productDto,
                          Model model) {
        model.addAttribute("product", productDto);
        return "product/add";
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(@AuthenticationPrincipal User user,
                      @Valid @ModelAttribute ProductDto productDto,
                      BindingResult bindingResult,
                      @RequestPart("files") MultipartFile[] files,
                      RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            return "product/add";
        }
        try {
            Product product = productService.add(user, productDto, files);

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생하였습니다.");
            return "redirect:/product/add";
        }

        return "redirect:/product/list";

    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {

        Product product = productService.findById(id);
        productService.plusViewCount(product);
        model.addAttribute("product", product);
        model.addAttribute("imgList", product.getFileList());

        return "product/detail";
    }

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product/modify";
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(@AuthenticationPrincipal User user,
                         @PathVariable("id") Long id,
                         @Valid @ModelAttribute ProductDto productDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "product/modify";
        }

        Product product = productService.findById(id);

        Member member = memberService.findByUsername(user.getUsername());

        if (!productService.hasPermission(product, member)) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/product/detail/" + id;
        }

        productService.modify(member, product, productDto);


        return "redirect:/product/detail/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Long id,
                         @AuthenticationPrincipal User user,
                         RedirectAttributes redirectAttributes) {

        Product product = productService.findById(id);

        Member member = memberService.findByUsername(user.getUsername());

        if (!productService.hasPermission(product, member)) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/product/detail/" + id;
        }

        productService.delete(product);
        return "redirect:/product/list";
    }

    @GetMapping("/fileDelete/{fileId}")
    @PreAuthorize("isAuthenticated()")
    public String fileDelete(@PathVariable("fileId") Long fileId,
                             @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes) {

        FileUploadEntity file = fileService.findById(fileId);

        Product product = file.getProduct();

        Member member = memberService.findByUsername(user.getUsername());

        if (!productService.hasPermission(product, member)) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/product/detail/" + product.getId();
        }

        fileService.delete(file);

        return "redirect:/product/detail/" + product.getId();
    }

}
