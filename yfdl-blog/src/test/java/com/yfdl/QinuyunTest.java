package com.yfdl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.io.*;

@SpringBootTest
public class QinuyunTest {

    @Test
    public void test(){
        Te te = new Te();
        te.start();
    }

}

class Te extends Thread {
    @Override
    public void run() {
        super.run();
        System.out.println("fd");
    }
}