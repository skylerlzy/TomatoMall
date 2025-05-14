package com.example.tomatomall.service;

import com.example.tomatomall.vo.UserVO;

import java.util.List;

public interface UserService {
    UserVO getUserInformation(String username);

    String createUser(UserVO userVO);

    String login(String username, String password);

    String updateUserInformation(UserVO userVO);

    List<UserVO> getUserList();
}
