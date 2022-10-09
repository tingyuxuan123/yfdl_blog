package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public R upload(MultipartFile img){
        return uploadService.uploadImg(img);
    }

}