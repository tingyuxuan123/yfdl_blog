package com.yfdl.vo.follow;

import lombok.Data;

@Data
public class FollowListVo {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

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
}
