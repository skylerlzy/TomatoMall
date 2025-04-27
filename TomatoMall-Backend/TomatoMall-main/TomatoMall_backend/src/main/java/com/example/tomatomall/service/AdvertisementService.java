package com.example.tomatomall.service;

import com.example.tomatomall.vo.AdvertisementVO;

import java.util.List;

public interface AdvertisementService {
    List<AdvertisementVO> getAllAdvertisements();

    AdvertisementVO createAdvertisement(AdvertisementVO advertisementVO);

    String updateAdvertisement(AdvertisementVO advertisementVO);

    String deleteAdvertisement(Integer id);
}