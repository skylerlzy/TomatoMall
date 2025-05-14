package com.example.tomatomall.configure;

import com.example.tomatomall.enums.RoleEnum;
import com.example.tomatomall.po.User;
import com.example.tomatomall.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AccessInterceptor implements HandlerInterceptor {
    @Autowired
    SecurityUtil securityUtil;
    private static final Logger log = LoggerFactory.getLogger(AccessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));//支持跨域请求
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");//是否支持cookie跨域
        response.setHeader("Access-Control-Allow-Headers", "Authorization,Origin, X-Requested-With, Content-Type, Accept,Access-Token");//Origin, X-Requested-With, Content-Type, Accept,Access-Token
        return true;
    }

}

