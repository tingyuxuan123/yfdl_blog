package com.yfdl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * 角色和菜单关联表(RoleMenu)表实体类
 *
 * @author makejava
 * @since 2022-10-02 10:59:46
 */
@Data
@NoArgsConstructor
@TableName("sys_role_menu")
public class RoleMenuEntity implements Serializable {
    private static final long serialVersionUID = 599671088008524031L;


    /**
     * 角色ID
     */

    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
