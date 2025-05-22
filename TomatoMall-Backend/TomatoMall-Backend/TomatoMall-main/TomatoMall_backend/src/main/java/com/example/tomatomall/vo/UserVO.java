package com.example.tomatomall.vo;

import com.example.tomatomall.enums.RoleEnum;
import com.example.tomatomall.po.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class UserVO {

    private Integer id;           // 用户ID
    private String username;      // 用户名
    private String password;      // 用户密码
    private String name;          // 用户姓名
    private String avatar;        // 用户头像链接
    private RoleEnum role;          // 用户身份
    private String telephone;     // 用户手机号
    private String email;         // 用户邮箱
    private String location;
    private Integer isVIP;
    private Double wallet;
    private Double point;
    private List<Integer> collect;

    public User toPO(){
        User user=new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setName(this.name);
        user.setAvatar(this.avatar);
        user.setRole(this.role);
        user.setTelephone(this.telephone);
        user.setEmail(this.email);
        user.setLocation(this.location);
        user.setIsVIP(this.isVIP);
        user.setWallet(this.wallet);
        user.setPoint(this.point);
        user.setCollect(this.collect);
        return user;
    }
}