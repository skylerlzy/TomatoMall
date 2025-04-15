package com.example.tomatomall.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Stockpile;
import com.example.tomatomall.repository.StockpileRepository;
import com.example.tomatomall.vo.CartItemDTO;
import com.example.tomatomall.vo.CartListDTO;
import com.example.tomatomall.po.Cart;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.repository.CartRepository;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockpileRepository stockpileRepository;

    @Override
    public CartItemDTO addToCart(Integer userId, Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                    .orElseThrow(TomatoMallException::productNotFound);
        Stockpile stockpile = stockpileRepository.findByProductId(productId);
        if (stockpile.getAmount() < quantity) {
            throw TomatoMallException.AmountNotEnough();
        }

        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(userId, productId);
        Cart cart;
        if (existingCart.isPresent()) {
            cart = existingCart.get();
            int newQuantity = cart.getQuantity() + quantity;
            if (newQuantity > stockpile.getAmount()) {
                throw TomatoMallException.AmountNotEnough();
            }
            cart.setQuantity(newQuantity);
        } else {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
        }
        cartRepository.save(cart);

        return convertToDTO(cart, product);
    }

    @Override
    public void deleteCartItem(Integer userId, Integer cartItemId) {
        Cart cart = cartRepository.findByCartItemIdAndUserId(cartItemId, userId)
                .orElseThrow(TomatoMallException::CartNotFound);
        cartRepository.delete(cart);
    }

    @Override
    public void updateQuantity(Integer userId, Integer cartItemId, Integer quantity) {
        Cart cart = cartRepository.findByCartItemIdAndUserId(cartItemId, userId)
                .orElseThrow(TomatoMallException::CartNotFound);
        Stockpile stockpile = stockpileRepository.findByProductId(cart.getProductId());
        if (quantity > stockpile.getAmount()) {
            throw TomatoMallException.AmountNotEnough();
        }
        cart.setQuantity(quantity);
        cartRepository.save(cart);
    }

    @Override
    public CartListDTO getCartList(Integer userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        List<CartItemDTO> items = carts.stream().map(cart -> {
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(TomatoMallException::productNotFound);
            return convertToDTO(cart, product);
        }).collect(Collectors.toList());

        BigDecimal totalAmount = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartListDTO(items, items.size(), totalAmount);
    }

    private CartItemDTO convertToDTO(Cart cart, Product product) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(cart.getCartItemId());
        dto.setProductId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setPrice(BigDecimal.valueOf(product.getPrice()));
        dto.setDescription(product.getDescription());
        dto.setCover(product.getCover());
        dto.setDetail(product.getDetail());
        dto.setQuantity(cart.getQuantity());
        return dto;
    }
}