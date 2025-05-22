package com.example.tomatomall.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.service.InventoryService;
import com.example.tomatomall.vo.OrderDTO;
import com.example.tomatomall.vo.PayResponse;
import com.example.tomatomall.po.*;
import com.example.tomatomall.repository.*;
import com.example.tomatomall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.tomatomall.configure.AlipayConfig;
import com.example.tomatomall.enums.OrderStatusEnum;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockpileRepository stockpileRepository;

    @Autowired
    private CartsOrdersRelationRepository relationRepository;

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private CouponRepository couponRepository;

    @Override
    @Transactional
    public OrderDTO createOrder(Integer userId, List<String> cartItemIds, String shippingAddress, String paymentMethod) {
        List<Cart> cartItems = cartItemIds.stream()
                .map(Integer::valueOf)
                .map(cartItemId -> cartRepository.findByCartItemIdAndUserId(cartItemId, userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (cartItems.size() != cartItemIds.size()) {
            throw TomatoMallException.PartCartWrong();
        }

        // 检查库存
        for (Cart cart : cartItems) {
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(TomatoMallException::productNotFound);
            Stockpile stockpile = stockpileRepository.findByProductId(product.getId());
            if (stockpile.getAmount() < cart.getQuantity()) {
                throw TomatoMallException.AmountNotEnough();
            }
        }

        // 计算总金额
        BigDecimal totalAmount = cartItems.stream()
                .map(cart -> {
                    Product product = productRepository.findById(cart.getProductId()).get();
                    return BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(cart.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("PENDING");
        order.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Date lockTime = new Date();
        order.setLockTime(lockTime);
        orderRepository.save(order);

        // 锁定库存
        for (Cart cart : cartItems) {
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(TomatoMallException::productNotFound);
            Stockpile stockpile = stockpileRepository.findByProductId(product.getId());
            stockpile.setFrozen(stockpile.getFrozen() + cart.getQuantity());
            stockpile.setAmount(stockpile.getAmount() - cart.getQuantity());
            stockpile.setLockTime(lockTime);
            stockpileRepository.save(stockpile);
        }

        // 创建关联记录
        cartItems.forEach(cart -> {
            CartsOrdersRelation relation = new CartsOrdersRelation();
            relation.setCartitemId(cart.getCartItemId());
            relation.setOrderId(order.getOrderId());
            relationRepository.save(relation);
        });

        return convertToOrderDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO createWallet(Double amount, Integer userId, String paymentMethod) {
        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(BigDecimal.valueOf(amount));
        order.setPaymentMethod(paymentMethod);
        order.setStatus("PAID");
        order.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Date lockTime = new Date();
        order.setLockTime(lockTime);
        orderRepository.save(order);

        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            // 计算新积分：原积分 + amount*10
            double newPoint = user.getPoint() + (amount * 10);

            // 更新积分
            user.setPoint(newPoint);

            // 保存更新
            userRepository.save(user);
        });

        return convertToOrderDTO(order);
    }

   @Override
@Transactional
public void updateOrderPrice(Integer orderId, BigDecimal finalPrice, Integer couponId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(TomatoMallException::OrderNotFound);

    // 如果提供了优惠券 ID，验证优惠券是否可用
    if (couponId != null) {
       Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(TomatoMallException::CouponNotFound);

        // 检查优惠券数量是否足够
        if (coupon.getNumber() <= 0) {
            throw TomatoMallException.InvalidCoupon();
        }

        // 标记优惠券为已使用并减少数量
        coupon.setNumber(coupon.getNumber() - 1);
        couponRepository.save(coupon);
    }

    // 更新订单价格
    order.setTotalAmount(finalPrice);
    orderRepository.save(order);
}
    @Override
    @Transactional
    public OrderDTO createVIP(Integer isVIP, Integer userId, String paymentMethod) {
        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (isVIP == 1) {
            totalAmount = new BigDecimal(25);
        } else if (isVIP == 2) {
            totalAmount = new BigDecimal(40);
        }

        // 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("PAID");
        order.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Date lockTime = new Date();
        order.setLockTime(lockTime);
        orderRepository.save(order);

        return convertToOrderDTO(order);
    }

    @Override
    public PayResponse generatePaymentForm(Integer orderId) throws AlipayApiException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(TomatoMallException::OrderNotFound);
        User user = userRepository.findById(order.getUserId())
                .orElseThrow(TomatoMallException::userNotFound);

        if (Objects.equals(order.getPaymentMethod(), "钱包")) {
            if (order.getTotalAmount().compareTo(BigDecimal.valueOf(user.getWallet())) > 0) {
                throw TomatoMallException.balanceNotEnough();
            }
            double walletAmount = user.getWallet();
            BigDecimal totalAmount = order.getTotalAmount();
            BigDecimal walletAmountBigDecimal = BigDecimal.valueOf(walletAmount);
            BigDecimal newWalletAmount = walletAmountBigDecimal.subtract(totalAmount);
            user.setWallet(newWalletAmount.doubleValue());
            userRepository.save(user);

            // 更新订单状态（注意幂等性，防止重复处理）
            //updateOrderStatus(orderId, "null", null);
            if ("PAID".equals(order.getStatus())) {
                return new PayResponse(); // 幂等处理
            }
            order.setStatus("PAID");
            orderRepository.save(order);
            // 支付成功，释放锁定的库存
            List<CartsOrdersRelation> relations = relationRepository.findByOrderId(orderId);
            relations.forEach(relation -> {
                Cart cart = cartRepository.findById(relation.getCartitemId())
                        .orElseThrow(TomatoMallException::CartNotFound);
                Product product = productRepository.findById(cart.getProductId())
                        .orElseThrow(TomatoMallException::productNotFound);
                Stockpile stockpile = stockpileRepository.findByProductId(product.getId());
                stockpile.setFrozen(stockpile.getFrozen() - cart.getQuantity());
                stockpileRepository.save(stockpile);

                updateUserPreferences(product, user);

                product.setSales(product.getSales() + cart.getQuantity());
                productRepository.save(product);
                // 先删除关联表中的数据
                relationRepository.delete(relation);
                // 再删除购物车数据
                cartRepository.delete(cart);
            });
            inventoryService.reduceStock(orderId);
            return new PayResponse();
        } else if (Objects.equals(order.getPaymentMethod(), "积分")) {
            if (order.getTotalAmount().compareTo(BigDecimal.valueOf(user.getPoint())) > 0) {
                throw TomatoMallException.balanceNotEnough();
            }
            double pointAmount = user.getPoint();
            BigDecimal totalAmount = order.getTotalAmount();
            BigDecimal pointAmountBigDecimal = BigDecimal.valueOf(pointAmount);
            BigDecimal newPointAmount = pointAmountBigDecimal.subtract(totalAmount);
            user.setPoint(newPointAmount.doubleValue());
            userRepository.save(user);

            // 更新订单状态（注意幂等性，防止重复处理）
            //updateOrderStatus(orderId, "null", null);
            if ("PAID".equals(order.getStatus())) {
                return new PayResponse(); // 幂等处理
            }
            order.setStatus("PAID");
            orderRepository.save(order);
            // 支付成功，释放锁定的库存
            List<CartsOrdersRelation> relations = relationRepository.findByOrderId(orderId);
            relations.forEach(relation -> {
                Cart cart = cartRepository.findById(relation.getCartitemId())
                        .orElseThrow(TomatoMallException::CartNotFound);
                Product product = productRepository.findById(cart.getProductId())
                        .orElseThrow(TomatoMallException::productNotFound);
                Stockpile stockpile = stockpileRepository.findByProductId(product.getId());
                stockpile.setFrozen(stockpile.getFrozen() - cart.getQuantity());
                stockpileRepository.save(stockpile);

                updateUserPreferences(product, user);

                product.setSales(product.getSales() + cart.getQuantity());
                productRepository.save(product);
                // 先删除关联表中的数据
                relationRepository.delete(relation);
                // 再删除购物车数据
                cartRepository.delete(cart);
            });
            inventoryService.reduceStock(orderId);
            return new PayResponse();
        }

        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayConfig.getServerUrl(),
                alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(),
                "json",
                "UTF-8",
                alipayConfig.getAlipayPublicKey(),
                "RSA2");

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        request.setReturnUrl(alipayConfig.getReturnUrl());

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", order.getOrderId().toString());
        bizContent.put("total_amount", order.getTotalAmount().setScale(2).toString());
        bizContent.put("subject", "订单支付");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        AlipayTradePagePayResponse paymentForm = alipayClient.pageExecute(request);

        PayResponse payResponse = new PayResponse();
        payResponse.setPaymentForm(paymentForm.getBody());
        payResponse.setOrderId(order.getOrderId());
        payResponse.setTotalAmount(order.getTotalAmount());
        payResponse.setPaymentMethod("Alipay");
        return payResponse;
    }

    @Override
@Transactional
public void updateOrderStatus(Integer orderId, String alipayTradeNo, BigDecimal amount) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(TomatoMallException::OrderNotFound);
    if ("PAID".equals(order.getStatus())) {
        return; // 幂等处理
    }
    order.setStatus("PAID");
    orderRepository.save(order);


    // 支付成功，释放锁定的库存
    List<CartsOrdersRelation> relations = relationRepository.findByOrderId(orderId);
    relations.forEach(relation -> {
        Cart cart = cartRepository.findById(relation.getCartitemId())
                .orElseThrow(TomatoMallException::CartNotFound);
        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(TomatoMallException::productNotFound);
        Stockpile stockpile = stockpileRepository.findByProductId(product.getId());
        stockpile.setFrozen(stockpile.getFrozen() - cart.getQuantity());
        stockpileRepository.save(stockpile);

        User user = userRepository.findById(cart.getUserId())
                .orElseThrow(TomatoMallException::userNotFound);
        updateUserPreferences(product, user);

        product.setSales(product.getSales() + cart.getQuantity());
        productRepository.save(product);
        // 先删除关联表中的数据
        relationRepository.delete(relation);
        // 再删除购物车数据
        cartRepository.delete(cart);
    });
}
    private OrderDTO convertToOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setUsername(userRepository.findById(order.getUserId()).get().getUsername());
        dto.setOrderId(order.getOrderId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setCreateTime(order.getCreateTime());
        dto.setStatus(order.getStatus());
        return dto;
    }

    // 定时任务，检查超时未支付的订单，释放锁定的库存
    @Scheduled(fixedRate = 60 * 1000) // 每分钟检查一次
    public void checkExpiredOrders() {
        long lockTimeLimit = 30 * 60 * 1000; // 30分钟
        Date now = new Date();
        List<Order> expiredOrders = orderRepository.findByStatusAndLockTimeBefore("PENDING", new Date(now.getTime() - lockTimeLimit));
        expiredOrders.forEach(order -> {
            List<CartsOrdersRelation> relations = relationRepository.findByOrderId(order.getOrderId());
            relations.forEach(relation -> {
                Cart cart = cartRepository.findById(relation.getCartitemId())
                        .orElseThrow(TomatoMallException::CartNotFound);
                Product product = productRepository.findById(cart.getProductId())
                        .orElseThrow(TomatoMallException::productNotFound);
                Stockpile stockpile = stockpileRepository.findByProductId(product.getId());
                stockpile.setAmount(stockpile.getAmount() + cart.getQuantity());
                stockpile.setFrozen(stockpile.getFrozen() - cart.getQuantity());
                stockpile.setLockTime(null);
                stockpileRepository.save(stockpile);
            });
            order.setStatus("TIMEOUT");
            orderRepository.save(order);
        });
    }

    public void updateUserPreferences(Product product, User user) {
        if (product.getAuthor() != null && !product.getAuthor().isEmpty()) {
            List<String> likeAuthor = user.getLikeAuthor();
            if (likeAuthor == null) {
                likeAuthor = new ArrayList<>();
                user.setLikeAuthor(likeAuthor);
            }
            likeAuthor.add(product.getAuthor());
            user.setLikeAuthor(likeAuthor);
        }

        if (product.getTag() != null && !product.getTag().isEmpty()) {
            List<String> likeTag = user.getLikeTag();
            if (likeTag == null) {
                likeTag = new ArrayList<>();
                user.setLikeTag(likeTag);
            }
            likeTag.add(product.getTag());
            user.setLikeTag(likeTag);
        }
        userRepository.save(user);
    }
}