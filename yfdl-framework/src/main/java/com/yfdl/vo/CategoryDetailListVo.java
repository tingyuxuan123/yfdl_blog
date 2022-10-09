package com.yfdl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetailListVo {

    private Long id;

    /**
     * 分类名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 文章数量
     */
    private Long articleCount;

    /**
     * 状态0:正常,1禁用
     */
    private String status;

}
