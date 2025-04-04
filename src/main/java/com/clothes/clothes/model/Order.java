package com.clothes.clothes.model;

import java.math.BigDecimal;
import java.util.List;

public class Order {
        private int orderId;
        private int userId;
        private String username;
        private List<OrderDetail> orderDetails;
        private BigDecimal totalAmount;
        private String shippingAddress;
        private String phone;

        public Order() {
        }
        public Order(int orderId, int userId, String username, BigDecimal totalAmount,
                    String shippingAddress, String phone) {
            this.orderId = orderId;
            this.userId = userId;
            this.username = username;
            this.totalAmount = totalAmount;
            this.shippingAddress = shippingAddress;
            this.phone = phone;
        }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
