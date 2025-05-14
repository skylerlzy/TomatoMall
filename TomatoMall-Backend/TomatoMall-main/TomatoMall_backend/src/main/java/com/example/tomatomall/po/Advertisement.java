package com.example.tomatomall.po;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "advertisements")
@Data
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT COMMENT '广告id'")
    private Integer id;

    @Column(name = "title", nullable = false, length = 50, columnDefinition = "VARCHAR(50) NOT NULL COMMENT '广告标题，不允许为空'")
    private String title;

    @Column(name = "content", nullable = false, length = 500, columnDefinition = "VARCHAR(500) NOT NULL COMMENT '广告内容'")
    private String content;

    @Column(name = "image_url", nullable = false, length = 500, columnDefinition = "VARCHAR(500) NOT NULL COMMENT '广告图片url'")
    private String imageUrl;

    @Column(name = "product_id", nullable = false, columnDefinition = "INT NOT NULL COMMENT '所属商品id，不允许为空'")
    private Integer productId;
}