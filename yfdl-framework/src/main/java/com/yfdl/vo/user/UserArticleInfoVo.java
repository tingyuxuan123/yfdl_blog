package com.yfdl.vo.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserArticleInfoVo {

    private Long articleCount;

    private Long likeCount;

    private Long readCount;

    private Date createTime;

}
