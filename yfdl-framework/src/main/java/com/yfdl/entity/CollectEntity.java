package com.yfdl.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * (Collect)表实体类
 *
 * @author makejava
 * @since 2022-11-13 20:07:31
 */
@Data
@NoArgsConstructor
@TableName("sg_collect")
public class CollectEntity implements Serializable {
    private static final long serialVersionUID = -77247178949048759L;

    
        /**
        * id
        */
        @TableId(value = "id", type = IdType.AUTO)
        private Long id;

        /**
    * 收藏集的id
    */
            private Long collectionId;

        /**
    * 收藏文章的id
    */
            private Long articleId;

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
    * 逻辑删除 0 为删除 1 为删除
    */
            private String delFlag;

}
