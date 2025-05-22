package com.example.tomatomall.service;

import com.example.tomatomall.vo.CouponVO;

import java.util.List;

public interface CouponService {
    CouponVO add(CouponVO coupon);
    String delete(Integer id);
    String update(CouponVO coupon);
    List<CouponVO> getList();
    CouponVO get(Integer id);
}
