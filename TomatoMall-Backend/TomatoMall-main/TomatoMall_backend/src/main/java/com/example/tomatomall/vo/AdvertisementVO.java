package com.example.tomatomall.vo;

import com.example.tomatomall.po.Advertisement;
import lombok.Data;

@Data
public class AdvertisementVO {
    private Integer id;
    private String title;
    private String content;
    private String imgUrl;
    private Integer productId;

    // 转换方法：PO -> VO
    public static AdvertisementVO fromPO(Advertisement po) {
        AdvertisementVO vo = new AdvertisementVO();
        vo.setId(po.getId());
        vo.setTitle(po.getTitle());
        vo.setContent(po.getContent());
        vo.setImgUrl(po.getImageUrl());
        vo.setProductId(po.getProductId());
        return vo;
    }

    // 转换方法：VO -> PO
    public Advertisement toPO() {
        Advertisement po = new Advertisement();
        po.setId(this.id);
        po.setTitle(this.title);
        po.setContent(this.content);
        po.setImageUrl(this.imgUrl);
        po.setProductId(this.productId);
        return po;
    }
}