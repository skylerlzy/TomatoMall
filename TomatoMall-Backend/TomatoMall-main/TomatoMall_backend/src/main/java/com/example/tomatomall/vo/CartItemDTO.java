package com.example.tomatomall.vo;

import java.math.BigDecimal;

public class CartItemDTO {
    private Integer cartItemId;
    private Integer productId;
    private String title;
    private BigDecimal price;
    private String description;
    private String cover;
    private String detail;
    private Integer quantity;

    // Getters and Setters
    public Integer getCartItemId() { return cartItemId; }
    public void setCartItemId(Integer cartItemId) { this.cartItemId = cartItemId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}