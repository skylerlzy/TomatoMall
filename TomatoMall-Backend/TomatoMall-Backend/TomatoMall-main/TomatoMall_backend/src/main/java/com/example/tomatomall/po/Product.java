package com.example.tomatomall.po;

import javax.persistence.*;
import com.example.tomatomall.vo.ProductVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column
    private double price;

    // 在实体类中使用转换器
    @Column(name = "rate_list")
    @Convert(converter = DoubleListConverter.class)
    private List<Double> rateList;

    @Basic
    @Column(name = "rate")
    private Double rate;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "cover")
    private String cover;

    @Basic
    @Column(name = "detail")
    private String detail;

    @Basic
    @Column(name = "sales")
    private Integer sales;

    @Basic
    @Column(name = "tag")
    private String tag;

    @Basic
    @Column(name = "author")
    private String author;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SpecificationPO> specifications;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Stockpile stockpile;

    // 在setRateList时计算rate字段的值
    public void setRateList(List<Double> rateList) {
        this.rateList = rateList;
        if (rateList != null && !rateList.isEmpty()) {
            this.rate = rateList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @Entity
    @Table(name = "specification")
    public static class SpecificationPO {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "item")
        private String item;

        @Column(name = "value")
        private String value;

        @Column(name = "product_id")
        private Integer productId;

        @ManyToOne
        @JoinColumn(name = "product_id", insertable = false, updatable = false)
        private Product product;

        public ProductVO.SpecificationVO toVO() {
            ProductVO.SpecificationVO specificationVO = new ProductVO.SpecificationVO();
            specificationVO.setId(id);
            specificationVO.setItem(item);
            specificationVO.setValue(value);
            specificationVO.setProductId(productId);
            return specificationVO;
        }
    }

    public ProductVO toVO(){
        ProductVO productVO = new ProductVO();
        productVO.setId(id);
        productVO.setTitle(title);
        productVO.setPrice(price);
        productVO.setRate(rate);
        productVO.setSales(sales);
        productVO.setDescription(description);
        productVO.setCover(cover);
        productVO.setDetail(detail);
        productVO.setTag(tag);
        productVO.setAuthor(author);
        productVO.setSpecifications(specificationsToVO());
        return productVO;
    }

    public List<ProductVO.SpecificationVO> specificationsToVO() {
        List<ProductVO.SpecificationVO> specificationVOs = new ArrayList<>();
        for (SpecificationPO specificationPO : specifications) {
            specificationVOs.add(specificationPO.toVO());
        }
        return specificationVOs;
    }
}
