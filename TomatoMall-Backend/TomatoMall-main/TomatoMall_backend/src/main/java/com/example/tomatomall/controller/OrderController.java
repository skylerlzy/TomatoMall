package com.example.tomatomall.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.example.tomatomall.configure.AlipayConfig;
<<<<<<< HEAD
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Order;
import com.example.tomatomall.service.CartService;
=======
>>>>>>> a782c9bf154452cb686b2e8c8bc6b67b2097d3fe
import com.example.tomatomall.service.InventoryService;
import com.example.tomatomall.util.SecurityUtil;
import com.example.tomatomall.vo.AliPay;
import com.example.tomatomall.vo.ResultVO;
import com.example.tomatomall.vo.OrderDTO;
import com.example.tomatomall.vo.PayResponse;
import com.example.tomatomall.service.OrderService;
<<<<<<< HEAD
import com.example.tomatomall.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

>>>>>>> a782c9bf154452cb686b2e8c8bc6b67b2097d3fe
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
<<<<<<< HEAD
import java.util.HashMap;
=======
>>>>>>> a782c9bf154452cb686b2e8c8bc6b67b2097d3fe
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OrderController {

<<<<<<< HEAD
     @Autowired
    private OrderRepository orderRepository;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

=======
>>>>>>> a782c9bf154452cb686b2e8c8bc6b67b2097d3fe
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

<<<<<<< HEAD

@GetMapping("/api/orders/{orderId}/status")
public ResultVO<Map<String, String>> getOrderStatus(@PathVariable Integer orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(TomatoMallException::OrderNotFound);
    Map<String, String> result = new HashMap<>();
    result.put("status", order.getStatus());
    return ResultVO.buildSuccess(result);
}
=======
    @PostMapping("/api/orders/notify")
    public void handleAlipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, AlipayApiException {
        // 1. 解析支付宝回调参数（通常是 application/x-www-form-urlencoded）
        Map<String, String> params = request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));

        // 2. 验证支付宝签名（防止伪造请求）
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), "UTF-8", "RSA2");
        if (!signVerified) {
            response.getWriter().print("fail"); // 签名验证失败，返回 fail
            return;
        }

        // 3. 处理业务逻辑（更新订单、减库存等）
        String tradeStatus = params.get("trade_status");
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            String orderId = params.get("out_trade_no"); // 您的订单号
            String alipayTradeNo = params.get("trade_no"); // 支付宝交易号
            String amount = params.get("total_amount"); // 支付金额

            // 更新订单状态（注意幂等性，防止重复处理）
            orderService.updateOrderStatus(Integer.valueOf(orderId), alipayTradeNo, BigDecimal.valueOf(Long.parseLong(amount)));

            // 扣减库存（建议加锁或乐观锁）
            inventoryService.reduceStock(Integer.valueOf(orderId));
        }

        // 4. 必须返回纯文本的 "success"（支付宝要求）
        response.getWriter().print("success");
    }
>>>>>>> a782c9bf154452cb686b2e8c8bc6b67b2097d3fe
}