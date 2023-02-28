package com.yfdl.vo.collection;

import com.yfdl.entity.CollectionEntity;
import com.yfdl.vo.ArticleListVo;
import com.yfdl.vo.PageVo;
import lombok.Data;

@Data
public class CollectionInfoVo {
    PageVo<ArticleListVo> articleListVo;
    CollectionEntity collection;

}
