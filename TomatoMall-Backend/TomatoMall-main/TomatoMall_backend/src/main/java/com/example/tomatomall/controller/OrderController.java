package com.example.tomatomall.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.example.tomatomall.configure.AlipayConfig;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Order;
import com.example.tomatomall.service.CartService;
import com.example.tomatomall.service.InventoryService;
import com.example.tomatomall.util.SecurityUtil;
import com.example.tomatomall.vo.AliPay;
import com.example.tomatomall.vo.ResultVO;
import com.example.tomatomall.vo.OrderDTO;
import com.example.tomatomall.vo.PayResponse;
import com.example.tomatomall.service.OrderService;
import com.example.tomatomall.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OrderController {

     @Autowired
    private OrderRepository orderRepository;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/api/cart/checkout")
    public ResultVO<OrderDTO> checkout(@RequestParam List<String> cartItemIds, String shipping_address, String payment_method) {
        OrderDTO order = orderService.createOrder(securityUtil.getCurrentUser().getId(), cartItemIds, shipping_address, payment_method);
        return ResultVO.buildSuccess(order);
    }

    @PostMapping("/api/orders/{orderId}/pay")
    public ResultVO<PayResponse> payOrder(@PathVariable Integer orderId) throws Exception {
        PayResponse response = orderService.generatePaymentForm(orderId);
        return ResultVO.buildSuccess(response);
    }


@GetMapping("/api/orders/{orderId}/status")
public ResultVO<Map<String, String>> getOrderStatus(@PathVariable Integer orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(TomatoMallException::OrderNotFound);
    Map<String, String> result = new HashMap<>();
    result.put("status", order.getStatus());
    return ResultVO.buildSuccess(result);
}
}