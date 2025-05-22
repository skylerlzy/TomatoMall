package com.example.tomatomall.exception;

public class TomatoMallException extends RuntimeException {
    public TomatoMallException(String message) {
        super(message);
    }
    public static TomatoMallException userAlreadyExists() {
        return new TomatoMallException("用户已经存在");
    }
    public static TomatoMallException userNotFound() {
        return new TomatoMallException("用户不存在");
    }
    public static TomatoMallException invalidPassword() {
        return new TomatoMallException("用户名或密码错误");
    }
    public static TomatoMallException notLogin() {return new TomatoMallException("用户没有登录");}

    public static TomatoMallException productNotFound() {return new TomatoMallException("商品不存在");}

    public static TomatoMallException AmountNotEnough() {return new TomatoMallException("库存不足");}
    public static TomatoMallException CartNotFound() {return new TomatoMallException("购物车商品不存在");}
    public static TomatoMallException PartCartWrong() {return new TomatoMallException("部分购物车商品信息无效");}
    public static TomatoMallException OrderNotFound() {return new TomatoMallException("订单不存在");}

    public static TomatoMallException AdNotFound() {return new TomatoMallException("商品不存在");}

    public static TomatoMallException fileUploadFail() {return new TomatoMallException("文件上传失败");}

    public static TomatoMallException couponNotFound() {return new TomatoMallException("优惠券不存在");}

    public static TomatoMallException commentNotFound() {return new TomatoMallException("评论不存在");}

    public static TomatoMallException wrongRate() {return new TomatoMallException("评分存储错误");}

    public static TomatoMallException wrongComment() {return new TomatoMallException("评论存储错误");}

    public static TomatoMallException CouponNotFound() {return new TomatoMallException("优惠券不存在");}

    public static TomatoMallException InvalidCoupon() {return new TomatoMallException("无效的优惠券");}

    public static TomatoMallException balanceNotEnough() {return new TomatoMallException("余额不足");}
}
