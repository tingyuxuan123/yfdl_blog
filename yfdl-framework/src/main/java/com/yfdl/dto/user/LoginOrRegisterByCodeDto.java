package com.yfdl.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@ApiModel(description = "验证码登录或注册的数据")
@Data
public class LoginOrRegisterByCodeDto {
    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("验证码")
    private String code;
}
