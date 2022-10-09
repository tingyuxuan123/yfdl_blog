package com.yfdl.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2022-09-25 19:28:22
 */
@Data
@NoArgsConstructor
@TableName("sys_user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 409703452429747431L;


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户类型：0代表普通用户，1代表管理员
     */
    private String type;

    /**
     * 账号状态（0正常 1停用）
     */
    private String status;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phonenumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    private String sex;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 个人说明
     */
    private String label;

    /**
     * 创建人的用户id
     */
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
