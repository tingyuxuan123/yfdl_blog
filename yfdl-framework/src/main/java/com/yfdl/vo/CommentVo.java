package com.yfdl.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {

    private Long id;

//    /**
//     * 评论类型（0代表文章评论，1代表友链评论）
//     */
//    private String type;

    private List<CommentVo> children;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 根评论id
     */
    private Long rootId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论用户
     */
    private String username;

    /**
     * 评论用户的头像
     */
    private String avatar;

    /**
     * 给评论回复的人的id
     */
    private Long toCommentUserId;

    /**
     * 给评论回复的人的用户名
     */
    private String toCommentUserName;

    /**
     * 给评论回复的人的头像
     */
    private String toCommentUserAvatar;

    /**
     * 回复目标评论id
     */
    private Long toCommentId;

    /**
     *  评论人
     */
    private Long createBy;

    /**
     * 评论时间
     */
    private Date createTime;

    private Integer likesCount;


}
