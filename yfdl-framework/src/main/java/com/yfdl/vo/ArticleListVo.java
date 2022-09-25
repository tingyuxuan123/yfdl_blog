package com.yfdl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListVo {

    /**
     * 文章id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 所属分类id
     */
    private String categoryName;

    /**
     * 缩略图
     */
    private String thumbnail;


    /**
     * 访问量
     */
    private Long viewCount;

    /**
     * 创建时间
     */
    private Date createTime;


}
