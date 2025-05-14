package com.example.tomatomall.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentVO {
    private Integer id;
    private Integer productId;
    private Integer userId;
    private Integer rate;
    private String image;
    private String content;
    private List<Integer> son;
    private Integer fatherId;
}
