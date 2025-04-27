package com.example.tomatomall.vo;

import com.example.tomatomall.po.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {
    private Integer id;
    private String title;
    private double price;
    private double rate;
    private String description;
    private String cover;
    private String detail;
    private List<SpecificationVO> specifications;
    private static Integer temp ;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SpecificationVO {
        private Integer id;
        private String item;
        private String value;
        private Integer productId;

        public Product.SpecificationPO toPO() {
            Product.SpecificationPO specificationPO = new Product.SpecificationPO();
            specificationPO.setId(id);
            specificationPO.setItem(item);
            specificationPO.setValue(value);
            specificationPO.setProductId(temp);
            return specificationPO;
        }
    }

    public List<Product.SpecificationPO> specificationsToPO() {
        List<Product.SpecificationPO> specificationPOs = new ArrayList<>();
        for (SpecificationVO specificationVO : specifications) {
            specificationPOs.add(specificationVO.toPO());
        }
        return specificationPOs;
    }

    public Product toPO() {
        Product product = new Product();
        product.setId(id);
        temp = id;
        product.setTitle(title);
        product.setPrice(price);
        product.setRate(rate);
        product.setDescription(description);
        product.setCover(cover);
        product.setDetail(detail);
        product.setSpecifications(specificationsToPO());
        return product;
    }
}

