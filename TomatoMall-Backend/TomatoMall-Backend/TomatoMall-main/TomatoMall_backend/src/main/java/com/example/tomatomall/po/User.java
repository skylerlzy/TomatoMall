package com.example.tomatomall.po;

import com.example.tomatomall.enums.RoleEnum;

import javax.persistence.*;

import com.example.tomatomall.vo.UserVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @Column(name = "isVIP")
    private Integer isVIP;

    @Basic
    @Column(name = "location")
    private String location;

    @Basic
    @Column(name = "wallet", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double wallet;

    @Basic
    @Column(name = "point", columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double point;

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "like_author", columnDefinition = "TEXT")
    private List<String> likeAuthor;

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "like_tag", columnDefinition = "TEXT")
    private List<String> likeTag;

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "collect", columnDefinition = "TEXT")
    private List<Integer> collect;

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
        userVO.setIsVIP(isVIP);
        userVO.setWallet(wallet);
        userVO.setPoint(point);
        userVO.setCollect(collect);
        return userVO;
    }
}