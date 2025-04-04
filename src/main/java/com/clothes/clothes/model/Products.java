package com.clothes.clothes.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Products {
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Categories category_id;
    private String imageUrl;

    public Products(Long productId, String name, BigDecimal price, Integer stock, Categories category_id, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category_id = category_id;
        this.imageUrl = imageUrl;
    }

}
