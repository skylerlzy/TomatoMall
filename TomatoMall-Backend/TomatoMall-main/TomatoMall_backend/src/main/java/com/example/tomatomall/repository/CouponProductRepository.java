package com.example.tomatomall.repository;

import com.example.tomatomall.po.CouponProduct;
import com.example.tomatomall.po.CouponProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CouponProductRepository extends JpaRepository<CouponProduct, CouponProductId> {
    void deleteByCouponId(Integer couponId);
    List<CouponProduct> findByCouponId(Integer couponId);
}