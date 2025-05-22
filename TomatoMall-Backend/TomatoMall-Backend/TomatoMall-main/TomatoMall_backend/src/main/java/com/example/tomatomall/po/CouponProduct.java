package com.example.tomatomall.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "coupon_products")
@IdClass(CouponProductId.class)
public class CouponProduct {
    @Id
    @Column(name = "coupon_id")
    private Integer couponId;

    @Id
    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private Coupon coupon;
}
