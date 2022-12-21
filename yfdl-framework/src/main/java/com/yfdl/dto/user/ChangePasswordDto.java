package com.yfdl.dto.user;

import lombok.Data;

@Data
public class ChangePasswordDto {

    /**
     * 密码
     */
    private String Password;

    /**
     * 新密码
     */
    private String NewPassword;
}
