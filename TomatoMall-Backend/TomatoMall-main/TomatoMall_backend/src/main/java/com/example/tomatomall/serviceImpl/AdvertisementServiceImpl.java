package com.example.tomatomall.serviceImpl;

import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Advertisement;
import com.example.tomatomall.repository.AdvertisementRepository;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.service.AdvertisementService;
import com.example.tomatomall.vo.AdvertisementVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private ProductRepository productRepository; // 假设有商品服务用于验证商品是否存在

    @Override
    public List<AdvertisementVO> getAllAdvertisements() {
        List<Advertisement> advertisementPOs = advertisementRepository.findAll();
        return advertisementPOs.stream()
                .map(AdvertisementVO::fromPO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdvertisementVO createAdvertisement(AdvertisementVO advertisementVO) {
        // 验证商品是否存在
        if (!productRepository.existsById(advertisementVO.getProductId())) {
            throw TomatoMallException.productNotFound();
        }

        Advertisement po = advertisementVO.toPO();
        Advertisement savedPO = advertisementRepository.save(po);
        return AdvertisementVO.fromPO(savedPO);
    }

    @Override
    @Transactional
    public String updateAdvertisement(AdvertisementVO advertisementVO) {
        // 验证商品是否存在
        if (!productRepository.existsById(advertisementVO.getProductId())) {
            throw TomatoMallException.productNotFound();
        }

        // 验证广告是否存在
        if (!advertisementRepository.existsById(advertisementVO.getId())) {
            throw TomatoMallException.AdNotFound();
        }

        Advertisement po = advertisementVO.toPO();
        advertisementRepository.save(po);
        return "更新成功";
    }

    @Override
    @Transactional
    public String deleteAdvertisement(Integer id) {
        if (!advertisementRepository.existsById(id)) {
            throw TomatoMallException.AdNotFound();
        }

        advertisementRepository.deleteById(id);
        return "删除成功";
    }
}