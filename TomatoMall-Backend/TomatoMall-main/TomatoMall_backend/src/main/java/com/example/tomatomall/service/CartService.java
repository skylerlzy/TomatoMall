package com.example.tomatomall.service;

import com.example.tomatomall.vo.CartItemDTO;
import com.example.tomatomall.vo.CartListDTO;

public interface CartService {
    CartItemDTO addToCart(Integer userId, Integer productId, Integer quantity);
    void deleteCartItem(Integer userId, Integer cartItemId);
    void updateQuantity(Integer userId, Integer cartItemId, Integer quantity);
    CartListDTO getCartList(Integer userId);
}