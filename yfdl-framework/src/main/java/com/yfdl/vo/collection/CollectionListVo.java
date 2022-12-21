package com.yfdl.vo.collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionListVo {

    /**
     * 收藏夹id
     */
    private Long id;

    /**
     * 收藏集名称
     */
    private String collectionName;

    /**
     * 描述
     */
    private String description;

    /**
     * 文章数
     */
    private Long collectCount;

    private Long isVisible;


}
