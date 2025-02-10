package com.example.demo.product.controller;

import com.example.demo.file.entity.FileUploadEntity;
import com.example.demo.file.service.FileService;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;
import com.example.demo.oauth.dto.PrincipalDetails;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
@Slf4j
public class ProductController {

    private final ProductService productService;

    private final FileService fileService;

    private final MemberService memberService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Page<Product> products = productService.findAll(page, keyword);
        model.addAttribute("paging", products);
        model.addAttribute("keyword", keyword);

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
    public String add(@AuthenticationPrincipal Object principal,
                      @Valid @ModelAttribute ProductDto productDto,
                      BindingResult bindingResult,
                      @RequestPart("files") MultipartFile[] files,
                      RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            return "product/add";
        }
        try {

            Member member = null;

            if (principal instanceof PrincipalDetails principalDetails) {
                member = principalDetails.getMember();
            } else if (principal instanceof OAuth2User oauth2User) {
                String provider = oauth2User.getAttribute("provider");
                String providerId = oauth2User.getAttribute("providerId");
                String username = provider + providerId;
                member = memberService.findByUsername(username);
            }

            Product product = productService.add(member, productDto, files);

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생하였습니다.");
            return "redirect:/product/add";
        }

        return "redirect:/product/list?page=0";

    }

    @GetMapping("/detail/{id}")
    public String detail(@AuthenticationPrincipal PrincipalDetails principal,
                         @PathVariable("id") Long id, Model model) {

        if (principal != null) {
            Member member = memberService.findByUsername(principal.getUsername());
            model.addAttribute("member", member);
            System.out.println(member.toString());
        }

        Product product = productService.findById(id);
        productService.plusViewCount(product);
        model.addAttribute("product", product);
        model.addAttribute("imgList", product.getFileList());

        return "product/detail";
    }

    @GetMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modifyForm(@PathVariable("id") Long id,
                             Model model,
                             ProductDto productDto) {
        Product product = productService.findById(id);
        model.addAttribute("productDto", productDto);
        model.addAttribute("product", product);
        return "product/modify";
    }

    @PostMapping("/modify/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(@AuthenticationPrincipal PrincipalDetails principalDetails,
                         @PathVariable("id") Long id,
                         @Valid @ModelAttribute ProductDto productDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         @RequestPart("files") MultipartFile[] files) throws IOException {
        if (bindingResult.hasErrors()) {
            return "product/modify";
        }

        Member member = principalDetails.getMember();

        Product product = productService.findById(id);

        try {
            if (!productService.hasPermission(product, member)) {
                redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
                return "redirect:/product/detail/" + id;
            }

            if (files != null && files.length > 1) {
                fileService.modify(files, product);
            }

            productService.modify(member, product, productDto);

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 문제가 생겼습니다.");
            e.printStackTrace();
            return "redirect:/product/modify/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "상품 수정 중 문제가 생겼습니다.");
            e.printStackTrace();
            return "redirect:/product/modify/" + id;
        }

        return "redirect:/product/detail/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(@PathVariable("id") Long id,
                         @AuthenticationPrincipal PrincipalDetails principalDetails,
                         RedirectAttributes redirectAttributes) {

        Member member = principalDetails.getMember();

        Product product = productService.findById(id);

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
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             RedirectAttributes redirectAttributes) {

        Member member = principalDetails.getMember();

        FileUploadEntity file = fileService.findById(fileId);

        Product product = file.getProduct();

        if (!productService.hasPermission(product, member)) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/product/detail/" + product.getId();
        }

        fileService.delete(file);

        return "redirect:/product/detail/" + product.getId();
    }

}
