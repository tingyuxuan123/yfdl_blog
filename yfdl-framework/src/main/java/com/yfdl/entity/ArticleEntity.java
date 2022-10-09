package com.yfdl.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * 文章表(Article)表实体类
 *
 * @author makejava
 * @since 2022-09-23 23:45:46
 */
@Data
@NoArgsConstructor
@TableName("sg_article")
public class ArticleEntity implements Serializable {
    private static final long serialVersionUID = -86803906532005976L;


    @TableId(value = "id", type = IdType.AUTO)
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
     * 分类名，表中不存在
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 是否置顶（0否，1是）
     */
    private String isTop;

    /**
     * 状态（0已发布，1草稿）
     */
    private String status;

    /**
     * 访问量
     */
    private Long viewCount;

    /**
     * 评论数
     */
    @TableField(exist = false)
    private Long commentCount;



    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

}
