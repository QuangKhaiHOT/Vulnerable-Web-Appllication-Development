package com.clothes.clothes.model;

import lombok.Getter;

import java.math.BigDecimal;
@Getter
public class OrderDetail {
        private int orderDetailId;
        private int orderId;
        private String productName;
        private String imageUrl;
        private BigDecimal price;
        private int quantity;
        private BigDecimal totalPrice;

        public OrderDetail() {
        }
        public OrderDetail(int orderDetailId, int orderId, String productName,
                           String imageUrl, BigDecimal price, int quantity) {
            this.orderDetailId = orderDetailId;
            this.orderId = orderId;
            this.productName = productName;
            this.imageUrl = imageUrl;
            this.price = price;
            this.quantity = quantity;
            this.totalPrice = price.multiply(new BigDecimal(quantity));
        }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
