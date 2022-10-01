package com.yfdl.service;

import com.yfdl.common.R;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    R uploadImg(MultipartFile img);
}
