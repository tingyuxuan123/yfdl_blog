package com.yfdl.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListVo {

    /**
     * 分类id
     */
    private Long id;

    /**
     * 分类名
     */
    private String name;

}
