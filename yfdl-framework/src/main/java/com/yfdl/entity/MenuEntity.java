package com.yfdl.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * 菜单权限表(Menu)表实体类
 *
 * @author makejava
 * @since 2022-09-29 15:31:08
 */
@Data
@NoArgsConstructor
@TableName("menu")
public class MenuEntity implements Serializable {
    private static final long serialVersionUID = -53451651312460783L;

    
        /**
    * 菜单ID
    */
            @TableId(value = "id", type = IdType.AUTO)
        private Long id;

        /**
    * 菜单名称
    */
            private String menuName;

        /**
    * 父菜单ID
    */
            private Long parentId;

        /**
    * 显示顺序
    */
            private Integer orderNum;

        /**
    * 路由地址
    */
            private String path;

        /**
    * 组件路径
    */
            private String component;

        /**
    * 是否为外链（0是 1否）
    */
            private Integer isFrame;

        /**
    * 菜单类型（M目录 C菜单 F按钮）
    */
            private String menuType;

        /**
    * 菜单状态（0显示 1隐藏）
    */
            private String visible;

        /**
    * 菜单状态（0正常 1停用）
    */
            private String status;

        /**
    * 权限标识
    */
            private String perms;

        /**
    * 菜单图标
    */
            private String icon;

        /**
    * 创建者
    */
            private Long createBy;

        /**
    * 创建时间
    */
            private Date createTime;

        /**
    * 更新者
    */
            private Long updateBy;

        /**
    * 更新时间
    */
            private Date updateTime;

        /**
    * 备注
    */
            private String remark;

            private String delFlag;

}