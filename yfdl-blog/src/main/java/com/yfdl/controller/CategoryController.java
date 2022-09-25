package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.entity.CategoryEntity;
import com.yfdl.service.CategoryService;
import com.yfdl.vo.CategoryListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;

    /**
     * 获取分类
     * @return
     */
    @GetMapping("getCategoryList")
    public R<List<CategoryListVo>> getCategoryList(){
        return categoryService.getCategoryList();
    }

}
