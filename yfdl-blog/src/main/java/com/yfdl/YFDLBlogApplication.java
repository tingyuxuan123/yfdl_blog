package com.yfdl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling  //表示可以使用定时任务
@EnableSwagger2   //使用swagger文档
public class YFDLBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(YFDLBlogApplication.class,args);
    }

}
