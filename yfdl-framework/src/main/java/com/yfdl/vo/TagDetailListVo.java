package com.yfdl.vo;

import lombok.Data;

@Data
public class TagDetailListVo {
    private Long id;

    /**
     * 分类名
     */
    private String name;

    /**
     * 主题图片
     */
    private String spanIcon;

    /**
     * 描述
     */
    private String remark;

    /**
     * 文章数量
     */
    private Long articleCount;
}
