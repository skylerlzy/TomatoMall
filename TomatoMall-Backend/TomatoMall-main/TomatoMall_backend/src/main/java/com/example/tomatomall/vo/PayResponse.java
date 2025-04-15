package com.example.tomatomall.vo;

import java.math.BigDecimal;

public class PayResponse {
    private String paymentForm;
    private Integer orderId;
    private BigDecimal totalAmount;
    private String paymentMethod;

    // Getters and Setters
    public String getPaymentForm() { return paymentForm; }
    public void setPaymentForm(String paymentForm) { this.paymentForm = paymentForm; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}