package com.example.tomatomall.serviceImpl;

import com.example.tomatomall.po.User;
import com.example.tomatomall.repository.UserRepository;
import com.example.tomatomall.service.UserService;
import com.example.tomatomall.util.SecurityUtil;
import com.example.tomatomall.util.TokenUtil;
import com.example.tomatomall.vo.UserVO;
import com.example.tomatomall.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserVO getUserInformation(String username) {
        User user = securityUtil.getCurrentUser();
        if (user == null) {
            throw TomatoMallException.userNotFound();
        }
        return user.toVO();
    }

    @Override
    public String createUser(UserVO userVO) {
        User user = userRepository.findByUsername(userVO.getUsername());
        if (user != null) {
            throw TomatoMallException.userAlreadyExists();
        }
        String rawPassword = userVO.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        userVO.setPassword(encodedPassword);
        User newUser = userVO.toPO();
        userRepository.save(newUser);
        return "注册成功";
    }

    @Override
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw TomatoMallException.userNotFound();
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw TomatoMallException.invalidPassword();
        }
        request.getSession().setAttribute("currentUser", user);
        return tokenUtil.getToken(user);
    }

    @Override
    public String updateUserInformation(UserVO userVO) {
        User user = securityUtil.getCurrentUser();
        if (userVO.getUsername() != null) {
            user.setUsername(userVO.getUsername());
        }
        if (userVO.getPassword() != null) {
            String rawPassword = userVO.getPassword();
            String encodedPassword = passwordEncoder.encode(rawPassword);
            user.setPassword(encodedPassword);
        }
        if (userVO.getName() != null) {
            user.setName(userVO.getName());
        }
        if (userVO.getEmail() != null) {
            user.setEmail(userVO.getEmail());
        }
        if (userVO.getAvatar() != null) {
            user.setAvatar(userVO.getAvatar());
        }
        if (userVO.getRole() != null) {
            user.setRole(userVO.getRole());
        }
        if (userVO.getTelephone() != null) {
            user.setTelephone(userVO.getTelephone());
        }
        if (userVO.getLocation() != null) {
            user.setLocation(userVO.getLocation());
        }
        userRepository.save(user);
        return "更新成功";
    }
}
