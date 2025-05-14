package com.example.tomatomall.po;

import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
public class CouponProductId implements Serializable {
    private Integer couponId;
    private Integer productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponProductId that = (CouponProductId) o;
        return Objects.equals(couponId, that.couponId) &&
               Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couponId, productId);
    }
}