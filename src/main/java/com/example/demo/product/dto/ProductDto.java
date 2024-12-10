package com.example.demo.product.dto;

import lombok.Data;

@Data
public class ProductDto {

    private String name;

    private String code;

    private String category;

    private String description;

    private int price;

    private int discount;
}
