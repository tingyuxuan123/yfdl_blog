package com.yfdl.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.common.config.OssConfig;
import com.yfdl.service.UploadService;
import com.yfdl.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
@Service

public class UploadServiceImpl implements UploadService {

    @Autowired
    private OssConfig ossConfig;

    @Override
    public R uploadImg(MultipartFile imgFile) {

        //获取原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        if (!(originalFilename.endsWith(".png") || originalFilename.endsWith(".jpg") || originalFilename.endsWith(".jpeg"))){
            throw  new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROE);
        }

        String url = ossUpload(imgFile);

        return R.successResult(url);
    }

    private String ossUpload(MultipartFile imgFile){

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);


        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = PathUtils.generateFilePath(imgFile.getOriginalFilename());

        try {

            Auth auth = Auth.create(ossConfig.getAccessKey(), ossConfig.getSecretKey());
            String upToken = auth.uploadToken(ossConfig.getBucket());
            InputStream inputStream = imgFile.getInputStream();



            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);


                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(ossConfig.getBaseurl().endsWith("/")){
            return ossConfig.getBaseurl() + key;
        }else {
            return ossConfig.getBaseurl() +"/"+ key;
        }


    }

}

