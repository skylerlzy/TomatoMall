package com.example.tomatomall.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.*;
import com.example.tomatomall.repository.*;
import com.example.tomatomall.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private CartsOrdersRelationRepository relationRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockpileRepository stockpileRepository;

    @Override
    @Transactional
    public void reduceStock(Integer orderId) {
        List<CartsOrdersRelation> relations = relationRepository.findByOrderId(orderId);
        relations.forEach(relation -> {
            Cart cart = cartRepository.findById(relation.getCartitemId())
                    .orElseThrow(TomatoMallException::CartNotFound);
            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(TomatoMallException::productNotFound);
            Stockpile stockpile = stockpileRepository.findByProductId(product.getId());
            stockpile.setAmount(stockpile.getAmount() - cart.getQuantity());
            productRepository.save(product);
            stockpileRepository.save(stockpile);
            cartRepository.delete(cart);
        });
    }
}