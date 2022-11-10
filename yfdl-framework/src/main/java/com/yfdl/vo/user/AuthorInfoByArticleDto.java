package com.yfdl.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorInfoByArticleDto {

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
