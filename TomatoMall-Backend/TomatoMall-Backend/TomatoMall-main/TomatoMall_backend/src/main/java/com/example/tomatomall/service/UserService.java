package com.example.tomatomall.service;

import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.ResultVO;
import com.example.tomatomall.vo.UserVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserService {
    String addCollect(Integer userId, Integer productId);

    String deleteCollect(Integer userId, Integer productId);

    List<ProductVO> getCollect(Integer userId);

    UserVO getUserInformation(String username);

    String createUser(UserVO userVO);

    String login(String username, String password);

    String updateUserInformation(UserVO userVO);
}
