package com.example.tomatomall.service;

import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.StockpileVO;

import java.util.List;

public interface ProductService {
    List<ProductVO> findAll();

    ProductVO findById(String id);

    String update(ProductVO productVO);

    ProductVO add(ProductVO productVO);

    String delete(String id);

    String amount(String productId, Integer amount);

    StockpileVO findAmount(String productId);
}
