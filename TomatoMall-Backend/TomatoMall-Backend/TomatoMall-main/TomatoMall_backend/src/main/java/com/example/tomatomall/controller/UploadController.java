package com.example.tomatomall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.tomatomall.service.UploadService;
import com.example.tomatomall.vo.ResultVO;

@RestController
@RequestMapping("/api")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping("/images")
    public ResultVO<String> upload(@RequestParam MultipartFile file){
        return ResultVO.buildSuccess(uploadService.upload(file));
    }

}
