package com.example.tomatomall.controller;

import java.util.List;

import com.example.tomatomall.po.Coupon;
import com.example.tomatomall.service.CouponService;
import com.example.tomatomall.vo.CouponVO;
import com.example.tomatomall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @PostMapping("/add")
    public ResultVO<CouponVO> add(@RequestBody CouponVO coupon) {
        return ResultVO.buildSuccess(couponService.add(coupon));
    }

    @DeleteMapping("/{id}")
    public ResultVO<String> delete(@PathVariable Integer id) {
        return ResultVO.buildSuccess(couponService.delete(id));
    }

    @PutMapping("/update")
    public ResultVO<String> update(@RequestBody CouponVO coupon) {
        return ResultVO.buildSuccess(couponService.update(coupon));
    }

    @GetMapping("/getList")
    public ResultVO<List<CouponVO>> getList() {
        return ResultVO.buildSuccess(couponService.getList());
    }

    @GetMapping("/{id}")
    public ResultVO<CouponVO> get(@PathVariable Integer id) {
        return ResultVO.buildSuccess(couponService.get(id));
    }
}
