package com.example.tomatomall.po;

import com.example.tomatomall.vo.UserVO;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;           // 用户ID
    private String username;      // 用户名
    private String password;      // 用户密码
    private String name;          // 用户姓名
    private String avatar;        // 用户头像链接
    private String role;          // 用户身份
    private String telephone;     // 用户手机号
    private String email;         // 用户邮箱
    private String location;      // 用户所在地

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserVO toVO(){
        UserVO userVO = new UserVO();
        userVO.setId(id);
        userVO.setUsername(username);
        userVO.setPassword(password);
        userVO.setName(name);
        userVO.setAvatar(avatar);
        userVO.setRole(role);
        userVO.setTelephone(telephone);
        userVO.setEmail(email);
        userVO.setLocation(location);
        return userVO;
    }
}
