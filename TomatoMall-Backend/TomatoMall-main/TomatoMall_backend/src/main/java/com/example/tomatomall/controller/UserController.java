package com.example.tomatomall.controller;

import com.example.tomatomall.po.User;
import com.example.tomatomall.service.UserService;
import com.example.tomatomall.util.TokenUtil;
import com.example.tomatomall.vo.ResultVO;
import com.example.tomatomall.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.tomatomall.util.SecurityUtil;
import com.example.tomatomall.exception.TomatoMallException;

@RestController
@RequestMapping("/api/accounts")
public class UserController {

    SecurityUtil securityUtil = new SecurityUtil();
    TokenUtil tokenUtil = new TokenUtil();

    @Autowired
    UserService userService;

    @GetMapping("/{username}")
    public ResultVO<UserVO> getUserInformation(@PathVariable String username){
        return ResultVO.buildSuccess(userService.getUserInformation(username));
    }

    @PostMapping("/register")
    public ResultVO<String> createUser(@RequestBody UserVO userVO){
        return ResultVO.buildSuccess(userService.createUser(userVO));
    }

    @PostMapping("/login")
    public ResultVO<String> login(@RequestBody UserVO userVO){
        return ResultVO.buildSuccess(userService.login(userVO.getUsername(), userVO.getPassword()));
    }

   @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResultVO<String> updateUserInformation(@RequestBody UserVO userVO) {
        return ResultVO.buildSuccess(userService.updateUserInformation(userVO));
    }

}
