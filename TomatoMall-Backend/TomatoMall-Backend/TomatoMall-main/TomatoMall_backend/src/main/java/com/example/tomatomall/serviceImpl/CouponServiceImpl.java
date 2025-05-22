package com.example.tomatomall.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Coupon;
import com.example.tomatomall.po.CouponProduct;
import com.example.tomatomall.repository.CouponProductRepository;
import com.example.tomatomall.repository.CouponRepository;
import com.example.tomatomall.service.CouponService;
import com.example.tomatomall.vo.CouponVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponProductRepository couponProductRepository;

    @Override
    @Transactional
    public CouponVO add(CouponVO couponVO) {
        // 转换VO到PO
        Coupon coupon = new Coupon();
        BeanUtils.copyProperties(couponVO, coupon, "products");
        coupon.setProducts(Collections.emptyList());

        // 保存主表
        Coupon savedCoupon = couponRepository.save(coupon);

        // 处理关联商品
        List<CouponProduct> products = Optional.ofNullable(couponVO.getProductIds())
                .orElse(Collections.emptyList())
                .stream()
                .map(productId -> {
                    CouponProduct cp = new CouponProduct();
                    cp.setCouponId(savedCoupon.getId());
                    cp.setProductId(productId);
                    return cp;
                })
                .collect(Collectors.toList());

        // 保存关联表
        couponProductRepository.saveAll(products);
        savedCoupon.setProducts(products);

        return toVO(savedCoupon);
    }

    @Override
    @Transactional
    public String delete(Integer id) {
        return couponRepository.findById(id)
                .map(coupon -> {
                    // 删除关联关系
                    couponProductRepository.deleteByCouponId(id);
                    // 删除主记录
                    couponRepository.deleteById(id);
                    return "删除成功";
                })
                .orElseThrow(TomatoMallException::couponNotFound);
    }

    @Override
    @Transactional
    public String update(CouponVO couponVO) {
        return couponRepository.findById(couponVO.getId())
                .map(existing -> {
                    // 更新主表
                    BeanUtils.copyProperties(couponVO, existing, "products");

                    // 更新关联表（先删后插）
                    couponProductRepository.deleteByCouponId(existing.getId());

                    List<CouponProduct> products = Optional.ofNullable(couponVO.getProductIds())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(productId -> {
                                CouponProduct cp = new CouponProduct();
                                cp.setCouponId(existing.getId());
                                cp.setProductId(productId);
                                return cp;
                            })
                            .collect(Collectors.toList());

                    couponProductRepository.saveAll(products);
                    existing.setProducts(products);

                    couponRepository.save(existing);
                    return "更新成功";
                })
                .orElseThrow(TomatoMallException::couponNotFound);
    }

    @Override
    public List<CouponVO> getList() {
        return couponRepository.findAll()
                .stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public CouponVO get(Integer id) {
        return couponRepository.findById(id)
                .map(this::toVO)
                .orElseThrow(TomatoMallException::couponNotFound);
    }

    private CouponVO toVO(Coupon coupon) {
        CouponVO vo = new CouponVO();
        BeanUtils.copyProperties(coupon, vo, "products");

        // 填充关联商品ID列表
        List<Integer> productIds = coupon.getProducts()
                .stream()
                .map(CouponProduct::getProductId)
                .collect(Collectors.toList());

        vo.setProductIds(productIds);
        return vo;
    }
}