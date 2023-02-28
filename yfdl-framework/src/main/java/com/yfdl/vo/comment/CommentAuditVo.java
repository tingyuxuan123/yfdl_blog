package com.yfdl.vo.comment;

import lombok.Data;
import java.util.Date;

@Data
public class CommentAuditVo {

    private Long id;

    //昵称
    private String nickName;

    //内容
    private String content;

    //状态审核
    private Integer audit;

    private String title;

    //发布日期
    private Date createTime;

    //评论文章
    private String articleName;

}
