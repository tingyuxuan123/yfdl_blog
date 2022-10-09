package com.yfdl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.NoArgsConstructor;
import lombok.Data;

/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2022-10-02 11:00:25
 */
@Data
@NoArgsConstructor
@TableName("sg_article_tag")
public class ArticleTagEntity implements Serializable {
    private static final long serialVersionUID = 281722730361146613L;


    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标签id
     */
    private Long tagId;

}
