package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.entity.CategoryEntity;
import com.yfdl.service.CategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 系统管理分类管理
     * 获取分类详情列表
     */
    @GetMapping("allCategoryDetailList")
    public R allCategoryDetailList(@RequestParam(defaultValue = "1") Long currentPage,
                                   @RequestParam(defaultValue = "10") Long pageSize,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String status) {


        return categoryService.allCategoryDetailList(currentPage,pageSize,name,status);
    }

    @PostMapping("insertCategory")
    public R insertCategory(@RequestBody CategoryEntity categoryEntity){
        return categoryService.insertCategory(categoryEntity);
    }

    @PostMapping("updateCategory")
    public R updateCategory(@RequestBody CategoryEntity categoryEntity){
        return categoryService.updateCategory(categoryEntity);
    }

    @GetMapping("deleteCategory")
    public R deleteCategory(@RequestParam Long id){
        return categoryService.deleteCategory(id);
    }

}
