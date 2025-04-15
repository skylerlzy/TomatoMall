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


}
