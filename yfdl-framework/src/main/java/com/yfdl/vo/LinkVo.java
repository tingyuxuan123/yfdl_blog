package com.yfdl.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LinkVo {
    private Long id;

    private String name;

    private String logo;

    private String description;

    /**
     * 网站地址
     */
    private String address;



}
