package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类列表 :用作搜索框
     * @return
     */
    @GetMapping("allCategoryList")
    public R categoryList(){
        return categoryService.getAllCategoryList();
    }

    /**
     * 获取分类详情列表
     */
    @GetMapping("allCategoryDetail")
    public R allCategoryDetailList(){
        return categoryService.allCategoryDetailList();
    }


}
