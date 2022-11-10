package com.yfdl.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * 评论表(Comment)表实体类
 *
 * @author makejava
 * @since 2022-09-28 13:45:57
 */
@ApiModel(description = "评论的实体类")
@Data
@NoArgsConstructor
@TableName("sg_comment")
public class CommentEntity implements Serializable {
    private static final long serialVersionUID = -46907081778748235L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论类型（0代表文章评论，1代表友链评论）
     */
    @ApiModelProperty(notes = "评论类型（0代表文章评论，1代表友链评论）")
    private String type;

    /**
     * 文章id
     */
    @ApiModelProperty(notes = "文章id")
    private Long articleId;

    /**
     * 根评论id
     */
    private Long rootId;

    /**
     * 评论内容
     */
    private String content;

    /**
     *  被评论人的id
     */
    private Long toCommentUserId;


    /**
     * 回复目标评论id, 这个评论回复的是那个评论 ，这个那个评论的id
     */
    private Long toCommentId;

    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;

}
