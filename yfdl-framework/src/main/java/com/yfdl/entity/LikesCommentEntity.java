package com.yfdl.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * (LikesComment)表实体类
 *
 * @author makejava
 * @since 2022-11-15 22:56:09
 */
@Data
@NoArgsConstructor
@TableName("sg_likes_comment")
public class LikesCommentEntity implements Serializable {
    private static final long serialVersionUID = 176722018486795745L;

    
        /**
    * id
    */
        @TableId(value = "id", type = IdType.AUTO)
        private Long id;

        /**
    * 用户id
    */
            private Long userId;


    /**
     * 点赞的文章id
     */
          private Long articleId;

        /**
    * 点赞的评论id
    */
            private Long likeCommentId;

        /**
    * 创建人
    */
            @TableField(fill = FieldFill.INSERT)
            private Long createBy;

        /**
    * 创建时间
    */
            @TableField(fill = FieldFill.INSERT)
            private Date createTime;

        /**
    * 更新时间
    */
            @TableField(fill = FieldFill.UPDATE)
            private Date updateTime;

        /**
    * 更新人
    */
            @TableField(fill = FieldFill.UPDATE)
            private Long updateBy;

        /**
    * 逻辑删除（0 删除 1 未删除）
    */
            private String delFlag;

}
