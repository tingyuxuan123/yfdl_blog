package com.yfdl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

//@EnableSwagger2   //使用swagger文档
@SpringBootApplication
@EnableOpenApi
@EnableScheduling  //表示可以使用定时任务
public class YFDLBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(YFDLBlogApplication.class,args);
    }

}
