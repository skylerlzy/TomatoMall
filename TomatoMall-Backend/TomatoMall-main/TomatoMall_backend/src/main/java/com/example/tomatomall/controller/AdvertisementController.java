package com.example.tomatomall.controller;

import com.example.tomatomall.service.AdvertisementService;
import com.example.tomatomall.vo.AdvertisementVO;
import com.example.tomatomall.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @GetMapping
    public ResultVO<List<AdvertisementVO>> getAllAdvertisements() {
        List<AdvertisementVO> advertisements = advertisementService.getAllAdvertisements();
        return ResultVO.buildSuccess(advertisements);
    }

    @PostMapping
    public ResultVO<AdvertisementVO> createAdvertisement(@RequestBody AdvertisementVO advertisementVO) {
        AdvertisementVO createdAd = advertisementService.createAdvertisement(advertisementVO);
        return ResultVO.buildSuccess(createdAd);
    }

    @PutMapping
    public ResultVO<String> updateAdvertisement(@RequestBody AdvertisementVO advertisementVO) {
        String result = advertisementService.updateAdvertisement(advertisementVO);
        return ResultVO.buildSuccess(result);
    }

    @DeleteMapping("/{id}")
    public ResultVO<String> deleteAdvertisement(@PathVariable Integer id) {
        String result = advertisementService.deleteAdvertisement(id);
        return ResultVO.buildSuccess(result);
    }
}
