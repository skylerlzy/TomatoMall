package com.example.tomatomall.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartListDTO {
    private List<CartItemDTO> items;
    private Integer total;
    private BigDecimal totalAmount;

    // Constructor
    public CartListDTO(List<CartItemDTO> items, Integer total, BigDecimal totalAmount) {
        this.items = items;
        this.total = total;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public List<CartItemDTO> getItems() { return items; }
    public void setItems(List<CartItemDTO> items) { this.items = items; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}