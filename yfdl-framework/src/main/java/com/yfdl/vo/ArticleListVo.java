package com.yfdl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListVo {


    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;


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
     * 点赞数
     */
    private Long likesCount;

    /**
     * 评论数
     */

    private Integer commentCount=0;

    /**
     * 标签
     */

    private List<TagListVo> tags;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private Long createBy;

}


