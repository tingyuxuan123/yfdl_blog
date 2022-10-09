package com.yfdl.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yfdl.vo.TagListVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailDto implements Serializable {
    private static final long serialVersionUID = -86803906532005976L;
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 所属分类id
     */
    private Long categoryId;

    /**
     * 分类名，表中不存在
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 标签
     */

    private List<Long> tags;

    /**
     * 访问量
     */
    private Long viewCount;

    /**
     * 是否置顶
     */
    private String isTop;


    /**
     * 状态
     */
    private String status;

    /**
     * 是否允许评论 1是，0否
     */
    private String isComment;

    private Long createBy;

    private Date createTime;

}
