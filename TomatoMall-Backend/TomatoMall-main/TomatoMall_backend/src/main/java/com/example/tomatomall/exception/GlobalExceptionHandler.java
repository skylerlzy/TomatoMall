package com.example.tomatomall.exception;

import com.example.tomatomall.vo.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TomatoMallException.class)
    public ResultVO<String> handleBlueWhaleException(TomatoMallException e) {
        e.printStackTrace();
        if (e.getMessage().matches("用户没有登录")) {
            return ResultVO.notLogin();
        }
        return ResultVO.buildFailure(e.getMessage());
    }

    // 处理所有其他未被明确捕获的异常
    @ExceptionHandler(Exception.class)
    public ResultVO<String> handleGlobalException(Exception e) {
        e.printStackTrace();
        return ResultVO.buildFailure("请求处理失败: " + e.getMessage());
    }
}
