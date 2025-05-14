package com.example.tomatomall.util;

import com.example.tomatomall.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import com.example.tomatomall.util.TokenUtil;

@Component
public class SecurityUtil {

    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    TokenUtil tokenutil;

    public User getCurrentUser(){
        System.out.println(httpServletRequest.getSession().getAttribute("currentUser"));
        return (User)httpServletRequest.getSession().getAttribute("currentUser");
    }
}
