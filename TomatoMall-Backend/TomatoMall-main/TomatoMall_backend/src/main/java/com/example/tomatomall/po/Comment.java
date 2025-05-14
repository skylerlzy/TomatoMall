package com.example.tomatomall.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    private Integer rate;
    private String image;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "json")
    @Convert(converter = ListToJsonConverter.class)
    private List<Integer> son;

    @Column(name = "father_id")
    private Integer fatherId;
}
