package com.yfdl.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * (Collection)表实体类
 *
 * @author makejava
 * @since 2022-11-13 22:09:48
 */
@Data
@NoArgsConstructor
@TableName("sg_collection")
public class CollectionEntity implements Serializable {
    private static final long serialVersionUID = 634907846854518708L;

    
        /**
    * 收藏集id
    */
            @TableId(value = "id", type = IdType.AUTO)
        private Long id;

        /**
    * 收藏集名称
    */
            private String collectionName;

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
    * 更新时间
    */
        @TableField(fill = FieldFill.UPDATE)
        private Date updateTime;

        /**
    * 逻辑删除（0 未删除 1 删除）
    */
        private String delFlag;

        /**
    *  有这个收藏用户id
    */
        private Long userId;

        /**
    * 描述
    */
        private String description;

        /**
    * 是否显示 （0 显示 ，1隐藏）
    */

        private Integer isVisible;

}
