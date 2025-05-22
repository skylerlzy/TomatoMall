package com.example.tomatomall.serviceImpl;

import com.example.tomatomall.service.UploadService;
import com.example.tomatomall.util.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.tomatomall.exception.TomatoMallException;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    OssUtil ossUtil;

    @Override
    public String upload(MultipartFile file) {
        try {
            return ossUtil.upload(file.getOriginalFilename(),file.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
            throw TomatoMallException.fileUploadFail();
        }
    }

}