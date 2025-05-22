package com.example.tomatomall.repository;

import com.example.tomatomall.po.Coupon;
import com.example.tomatomall.po.CouponProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponProductRepository extends JpaRepository<CouponProduct, Integer> {
    void deleteByCouponId(Integer couponId);
}
