package com.yfdl.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleVo {

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
     * 文章内容
     */
    private String content;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 所属分类id
     */
    private Long categoryId;

    /**
     * 点赞数
     */
    private Long likesCount;

    /**
     * 是否点赞
     */
    private Boolean isLikes;

    /**
     * 评论数
     */
    private Long commentCount;


    /**
     * 分类名，表中不存在
     */
    private String categoryName;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 是否收藏
     */
    private Boolean isCollect=false;

    /**
     * 标签
     */

    private List<TagListVo> tags;

    /**
     * 访问量
     */
    private Long viewCount;

    /**
     * 是否置顶
     */
    private String isTop;


    /**
     * 状态
     */
    private String status;

    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;

    private Long createBy;

    private Date createTime;


}
