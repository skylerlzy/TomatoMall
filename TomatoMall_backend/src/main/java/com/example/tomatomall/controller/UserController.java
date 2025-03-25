package com.example.tomatomall.controller;

import com.example.tomatomall.service.UserService;
import com.example.tomatomall.vo.ResultVO;
import com.example.tomatomall.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{username}")
    public ResultVO<UserVO> getUserInformation(@RequestBody String username){
        return ResultVO.buildSuccess(userService.getUserInformation(username));
    }

    /*
    @PostMapping("/login")
    public ResultVO<String> login(@RequestParam("phone") String phone, @RequestParam("password") String password){
        return ResultVO.buildSuccess(userService.login(phone, password));
    }

    @GetMapping
    public ResultVO<UserVO> getInformation(){
        return ResultVO.buildSuccess(userService.getInformation());
    }

    @PostMapping
    public ResultVO<Boolean> updateInformation(@RequestBody UserVO userVO){
        return ResultVO.buildSuccess(userService.updateInformation(userVO));
    }
     */
}
