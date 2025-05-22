package com.example.tomatomall.controller;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.User;
import com.example.tomatomall.repository.UserRepository;
import com.example.tomatomall.service.UserService;
import com.example.tomatomall.util.TokenUtil;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.vo.ResultVO;
import com.example.tomatomall.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.tomatomall.util.SecurityUtil;

import java.util.*;

@RestController
@RequestMapping("/api/accounts")
public class UserController {

    @Autowired
    private SecurityUtil securityUtil;  // 使用依赖注入

    @Autowired
    private TokenUtil tokenUtil;  // 使用依赖注入


    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/getLikeAuthor")
    public ResultVO<List<List<Object>>> countLikeAuthor() {
        User user = userRepository.findById(securityUtil.getCurrentUser().getId())
                .orElseThrow(TomatoMallException::userNotFound);
        List<String> likeAuthor = user.getLikeAuthor();
        Map<String, Integer> authorCountMap = new LinkedHashMap<>(); // 保持插入顺序
        if (likeAuthor != null) {
            for (String author : likeAuthor) {
                if (author != null && !author.isEmpty()) {
                    authorCountMap.put(author, authorCountMap.getOrDefault(author, 0) + 1);
                }
            }
        }
        // 转换为二元数组格式
        List<List<Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : authorCountMap.entrySet()) {
            result.add(Arrays.asList(entry.getKey(), entry.getValue()));
        }
        return ResultVO.buildSuccess(result);
    }

    @GetMapping("/getLikeTag")
    public ResultVO<List<List<Object>>> countLikeTags() {
        User user = userRepository.findById(securityUtil.getCurrentUser().getId())
                .orElseThrow(TomatoMallException::userNotFound);
        List<String> likeTag = user.getLikeTag();

        Map<String, Integer> tagCountMap = new LinkedHashMap<>(); // 保持插入顺序
        if (likeTag != null) {
            for (String tag : likeTag) {
                if (tag != null && !tag.isEmpty()) {
                    tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
                }
            }
        }
        // 转换为二元数组格式
        List<List<Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : tagCountMap.entrySet()) {
            result.add(Arrays.asList(entry.getKey(), entry.getValue()));
        }

        return ResultVO.buildSuccess(result);
    }

    @PostMapping("/addCollect")
    public ResultVO<String> addCollect(@RequestParam Integer productId) {
        return ResultVO.buildSuccess(userService.addCollect(securityUtil.getCurrentUser().getId(), productId));
    }

    @DeleteMapping("/deleteCollect")
    public ResultVO<String> deleteCollect(@RequestParam Integer productId) {
        return ResultVO.buildSuccess(userService.deleteCollect(securityUtil.getCurrentUser().getId(), productId));
    }

    @GetMapping("/getCollect")
    public ResultVO<List<ProductVO>> getCollect() {
        return ResultVO.buildSuccess(userService.getCollect(securityUtil.getCurrentUser().getId()));
    }

    @GetMapping("/{username}")
    public ResultVO<UserVO> getUserInformation(@PathVariable String username){
        return ResultVO.buildSuccess(userService.getUserInformation(username));
    }

    @PostMapping("")
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
