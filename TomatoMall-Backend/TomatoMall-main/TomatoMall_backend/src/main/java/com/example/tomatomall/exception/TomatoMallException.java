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
}
