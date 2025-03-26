package com.example.tomatomall.po;

import com.example.tomatomall.vo.UserVO;
import com.example.tomatomall.enums.RoleEnum;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;


    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "avatar")
    private String avatar;

    @Basic
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Basic
    @Column(name = "telephone")
    private String telephone;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "location")
    private String location;

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
