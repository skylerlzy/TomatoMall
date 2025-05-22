package com.example.tomatomall.controller;

import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.util.TokenUtil;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.ResultVO;
import com.example.tomatomall.vo.StockpileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import com.example.tomatomall.util.SecurityUtil;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    SecurityUtil securityUtil = new SecurityUtil();
    TokenUtil tokenUtil = new TokenUtil();

    @Autowired
    ProductService productService;

    @PostMapping("/addRate")
    public ResultVO<Double> addRate(@RequestParam Double rate, Integer productId) {
        return ResultVO.buildSuccess(productService.addRate(rate, productId));
    }

    @GetMapping("/getSalesList")
    public ResultVO<List<ProductVO>> getSalesList(){
        return ResultVO.buildSuccess(productService.getSalesList());
    }

    @GetMapping("/getRateList")
    public ResultVO<List<ProductVO>> getRateList(){
        return ResultVO.buildSuccess(productService.getRateList());
    }

    @GetMapping("")
    public ResultVO<List<ProductVO>> findAll() {
        return ResultVO.buildSuccess(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResultVO<ProductVO> findById(@PathVariable String id) {
        return ResultVO.buildSuccess(productService.findById(id));
    }

    @PutMapping("")
    public ResultVO<String> update(@RequestBody ProductVO productVO) {
        return ResultVO.buildSuccess(productService.update(productVO));
    }

    @PostMapping("")
    public ResultVO<ProductVO> add(@RequestBody ProductVO productVO) {
        return ResultVO.buildSuccess(productService.add(productVO));
    }

    @DeleteMapping("{id}")
    public ResultVO<String> delete(@PathVariable String id) {
        return ResultVO.buildSuccess(productService.delete(id));
    }


    @PatchMapping("/stockpile/{productId}")
    public ResultVO<String> amount(@PathVariable String productId, @RequestBody StockpileVO stockpileVO) {
        return ResultVO.buildSuccess(productService.amount(productId, stockpileVO.getAmount()));
    }

    @GetMapping("/stockpile/{productId}")
    public ResultVO<StockpileVO> findAmount(@PathVariable String productId) {
        return ResultVO.buildSuccess(productService.findAmount(productId));
    }
}

