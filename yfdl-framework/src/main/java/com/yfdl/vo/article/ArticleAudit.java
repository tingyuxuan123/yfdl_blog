package com.yfdl.vo.article;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleAudit {
    //id
    private Long id;

    private String title;

    private Integer audit;

    /**
     * 是否置顶（0否，1是）
     */
    private String isTop;

    private Date createTime;

    private Date updateTime;

}
