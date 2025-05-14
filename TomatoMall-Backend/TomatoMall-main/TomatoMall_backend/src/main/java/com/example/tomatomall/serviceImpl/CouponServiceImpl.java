package com.example.tomatomall.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Coupon;
import com.example.tomatomall.po.CouponProduct;
import com.example.tomatomall.repository.CouponProductRepository;
import com.example.tomatomall.repository.CouponRepository;
import com.example.tomatomall.service.CouponService;
import com.example.tomatomall.po.CouponProductId;
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
        coupon.setUserId(couponVO.getUserId());
        coupon.setIsCommon(couponVO.getIsCommon());
        coupon.setVolume(couponVO.getVolume());
        coupon.setNeed(couponVO.getNeed());
        coupon.setDeadline(couponVO.getDeadline());
        coupon.setNumber(couponVO.getNumber());

        // 保存主表
        Coupon savedCoupon = couponRepository.save(coupon);

        // 处理关联商品
        if (couponVO.getProductIds() != null) {
            List<CouponProduct> products = couponVO.getProductIds().stream()
                .map(productId -> {
                    CouponProduct cp = new CouponProduct();
                    cp.setCouponId(savedCoupon.getId());
                    cp.setProductId(productId);
                    return cp;
                })
                .collect(Collectors.toList());

            // 保存关联表
            couponProductRepository.saveAll(products);
        }

        return get(savedCoupon.getId());
    }

    @Override
    public CouponVO get(Integer id) {
        return couponRepository.findById(id)
            .map(this::convertToVO)
            .orElseThrow(TomatoMallException::couponNotFound);
    }

    private CouponVO convertToVO(Coupon coupon) {
        CouponVO vo = new CouponVO();
        vo.setId(coupon.getId());
        vo.setUserId(coupon.getUserId());
        vo.setIsCommon(coupon.getIsCommon());
        vo.setVolume(coupon.getVolume());
        vo.setNeed(coupon.getNeed());
        vo.setDeadline(coupon.getDeadline());
        vo.setNumber(coupon.getNumber());

        List<Integer> productIds = couponProductRepository.findByCouponId(coupon.getId())
            .stream()
            .map(CouponProduct::getProductId)
            .collect(Collectors.toList());
        vo.setProductIds(productIds);

        return vo;
    }
@Override
@Transactional
public String delete(Integer id) {
    return couponRepository.findById(id)
        .map(coupon -> {
            // 先删除关联表数据
            couponProductRepository.deleteByCouponId(id);
            // 再删除主表数据
            couponRepository.deleteById(id);
            return "优惠券删除成功";
        })
        .orElseThrow(TomatoMallException::couponNotFound);
}
@Override
public List<CouponVO> getList() {
    return couponRepository.findAll().stream()
        .map(this::convertToVO)
        .collect(Collectors.toList());
}
    @Override
    @Transactional
    public String update(CouponVO couponVO) {
        return couponRepository.findById(couponVO.getId())
            .map(existing -> {
                // 更新主表字段
                existing.setUserId(couponVO.getUserId());
                existing.setIsCommon(couponVO.getIsCommon());
                existing.setVolume(couponVO.getVolume());
                existing.setNeed(couponVO.getNeed());
                existing.setDeadline(couponVO.getDeadline());
                existing.setNumber(couponVO.getNumber());

                // 更新关联表
                couponProductRepository.deleteByCouponId(existing.getId());

                if (couponVO.getProductIds() != null) {
                    List<CouponProduct> products = couponVO.getProductIds().stream()
                        .map(productId -> {
                            CouponProduct cp = new CouponProduct();
                            cp.setCouponId(existing.getId());
                            cp.setProductId(productId);
                            return cp;
                        })
                        .collect(Collectors.toList());

                    couponProductRepository.saveAll(products);
                }

                couponRepository.save(existing);
                return "更新成功";
            })
            .orElseThrow(TomatoMallException::couponNotFound);
    }
}