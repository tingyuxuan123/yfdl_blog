package com.yfdl.vo.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoByHomePageVo {
    private Long id;


    /**
     * 昵称
     */
    private String nickName;


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

    private String github;

    private String homepage;

    private String position;

    private String company;

    /**
     * 个人说明
     */
    private String label;


    /**
     * 是否关注
     */
    private Boolean isFollow;

    /**
     * 点赞数
     */
    private Long likesCount;

    /**
     * 阅读数
     */
    private Long readCount;

    /**
     * 关注数
     */
    private Long followCount;

    /**
     * 被关注数
     */
    private Long beFollowCount;


    /**
     *  收藏集数
     */
    private Long collectionCount;

    /**
     *  创建时间
     */
    private Date createTime;





}
