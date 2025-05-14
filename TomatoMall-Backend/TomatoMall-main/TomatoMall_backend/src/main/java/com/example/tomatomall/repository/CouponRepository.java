package com.example.tomatomall.repository;

import com.example.tomatomall.po.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
}
