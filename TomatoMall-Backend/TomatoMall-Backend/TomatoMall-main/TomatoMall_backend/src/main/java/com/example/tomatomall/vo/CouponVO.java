package com.example.tomatomall.vo;

import com.example.tomatomall.po.Coupon;
import com.example.tomatomall.po.CouponProduct;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// CouponVO.java
@Data
public class CouponVO {
    private Integer id;
    private Integer userId;
    private Boolean isCommon;
    private BigDecimal volume;
    private BigDecimal need;
    private LocalDate deadline;
    private Integer number;
    private List<Integer> productIds; // 关联的商品ID列表

    public static CouponVO fromEntity(Coupon coupon) {
        CouponVO vo = new CouponVO();
        vo.setId(coupon.getId());
        vo.setUserId(coupon.getUserId());
        vo.setIsCommon(coupon.getIsCommon());
        vo.setVolume(coupon.getVolume());
        vo.setNeed(coupon.getNeed());
        vo.setDeadline(coupon.getDeadline());
        vo.setNumber(coupon.getNumber());
        vo.setProductIds(coupon.getProducts().stream()
                .map(CouponProduct::getProductId)
                .collect(Collectors.toList()));
        return vo;
    }
}