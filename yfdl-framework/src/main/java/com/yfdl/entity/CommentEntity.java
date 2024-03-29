package com.yfdl.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * 评论表(Comment)表实体类
 *
 * @author makejava
 * @since 2022-11-13 22:08:47
 */
@Data
@NoArgsConstructor
@TableName("sg_comment")
public class CommentEntity implements Serializable {
    private static final long serialVersionUID = -10827583467376036L;

    
        @TableId(value = "id", type = IdType.AUTO)
        private Long id;

        /**
    * 评论类型（0代表文章评论，1代表友链评论）
    */
            private String type;

        /**
    * 文章id
    */
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
    * 所回复的目标评论的userid
    */
            private Long toCommentUserId;

        /**
    * 回复目标评论id
    */
            private Long toCommentId;

            @TableField(fill = FieldFill.INSERT)
            private Long createBy;

            @TableField(fill = FieldFill.INSERT)
            private Date createTime;

            @TableField(fill = FieldFill.UPDATE)
            private Long updateBy;

            @TableField(fill = FieldFill.UPDATE)
            private Date updateTime;

        /**
    * 删除标志（0代表未删除，1代表已删除）
    */
            private Integer delFlag;

        /**
    * 点赞数
    */

        private Integer likesCount;

    /**
     * 0通过 1未通过
     * */
    private Integer audit;
}
