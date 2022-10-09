package com.yfdl.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "登录参数")
public class LoginDto {


    @ApiModelProperty(notes="用户名")
    private String userName;



    /**
     * 密码
     */
    @ApiModelProperty(notes = "密码")
    private String password;

}
