package com.yfdl.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * (Follow)表实体类
 *
 * @author makejava
 * @since 2022-11-08 22:26:51
 */
@Data
@NoArgsConstructor
@TableName("sg_follow")
public class FollowEntity implements Serializable {
    private static final long serialVersionUID = 771415613924512557L;

    
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
    * 关注的用户
    */
            private Long followUserId;

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
    * 更新人
    */
            @TableField(fill = FieldFill.UPDATE)
            private Long updateBy;

        /**
    * 修改时间
    */
            @TableField(fill = FieldFill.UPDATE)
            private Date updateTime;

        /**
    * 逻辑删除（0 未删除 1 删除）
    */
            private String delFlag;

}
