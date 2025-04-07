package com.example.tomatomall.po;

import javax.persistence.*;
import com.example.tomatomall.vo.StockpileVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Stockpile {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_id")
    private Integer productId;

    @Basic
    @Column(name = "amount")
    private Integer amount;

    @Basic
    @Column(name = "frozen")
    private Integer frozen;

    //注意这里的外键设置
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;

    public StockpileVO toVO() {
        StockpileVO vo = new StockpileVO();
        vo.setId(id);
        vo.setProductId(productId);
        vo.setAmount(amount);
        vo.setFrozen(frozen);
        return vo;
    }

    public Stockpile(Integer productId, Integer amount, Integer frozen) {
        this.productId = productId;
        this.amount = amount;
        this.frozen = frozen;
    }
}
