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
     * 描述
     */
    private String remark;

    /**
     * 文章数量
     */
    private Long articleCount;
}
