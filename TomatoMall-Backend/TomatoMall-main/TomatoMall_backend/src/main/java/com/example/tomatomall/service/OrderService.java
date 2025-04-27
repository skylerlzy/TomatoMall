package com.example.tomatomall.service;

import com.example.tomatomall.vo.OrderDTO;
import com.example.tomatomall.vo.PayResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    OrderDTO createOrder(Integer userId, List<String> cartItemIds, String shippingAddress, String paymentMethod);
    PayResponse generatePaymentForm(Integer orderId) throws Exception;
    void updateOrderStatus(Integer orderId, String alipayTradeNo, BigDecimal amount);
}