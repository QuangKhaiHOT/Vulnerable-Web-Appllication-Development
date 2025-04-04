package com.clothes.clothes.model;

import java.math.BigDecimal;

public class CartDetail {
    private int cartId;
    private String username;
    private String productName;
    private String categoryName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;
    private String imageUrl;

    public CartDetail(int cartId, String username, String productName, String categoryName, BigDecimal price, int quantity, BigDecimal totalPrice, String imageUrl) {
        this.cartId = cartId;
        this.username = username;
        this.productName = productName;
        this.categoryName = categoryName;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.imageUrl = imageUrl;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
