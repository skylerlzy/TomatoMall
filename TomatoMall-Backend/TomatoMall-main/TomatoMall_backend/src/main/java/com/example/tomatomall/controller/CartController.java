package com.example.tomatomall.controller;

import com.example.tomatomall.util.SecurityUtil;
import com.example.tomatomall.vo.CartAddRequest;
import com.example.tomatomall.vo.CartItemDTO;
import com.example.tomatomall.vo.CartListDTO;
import com.example.tomatomall.vo.ResultVO;
import com.example.tomatomall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/api/cart")
    public ResultVO<CartItemDTO> addToCart(@RequestBody CartAddRequest request) {
        CartItemDTO item = cartService.addToCart(securityUtil.getCurrentUser().getId(), request.getProductId(), request.getQuantity());
        return ResultVO.buildSuccess(item);
    }

    @DeleteMapping("/api/cart/{cartItemId}")
    public ResultVO<String> deleteCartItem(@PathVariable Integer cartItemId) {
        cartService.deleteCartItem(securityUtil.getCurrentUser().getId(), cartItemId);
        return ResultVO.buildSuccess("删除成功");
    }

    @PatchMapping("/api/cart/{cartItemId}")
    public ResultVO<String> updateQuantity(@PathVariable Integer cartItemId, @RequestBody CartAddRequest request) {
        cartService.updateQuantity(securityUtil.getCurrentUser().getId(), cartItemId, request.getQuantity());
        return ResultVO.buildSuccess("修改数量成功");
    }

    @GetMapping("/api/cart")
    public ResultVO<CartListDTO> getCart() {
        CartListDTO cartList = cartService.getCartList(securityUtil.getCurrentUser().getId());
        return ResultVO.buildSuccess(cartList);
    }
}