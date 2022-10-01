package com.yfdl.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    private String accessKey;

    private String secretKey;

    private String bucket;

    private String baseurl;

}
