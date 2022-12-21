package com.yfdl.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    private String sex;

    private String email;


    /**
     * 手机号
     */
    private String phonenumber;



    private String github;

    private String homepage;

    private String position;

    private String company;

    /**
     * 个人说明
     */
    private String label;


}