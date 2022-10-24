package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.entity.CategoryEntity;
import com.yfdl.entity.TagEntity;
import com.yfdl.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("tagList")
    public R getTaglist(){
        return tagService.getTagList();
    }


    @GetMapping("queryTagListByParams")
    public R allCategoryDetailList(@RequestParam(defaultValue = "1") Long currentPage,
                                   @RequestParam(defaultValue = "10") Long pageSize,
                                   @RequestParam(required = false) String name
                                 ) {


        return tagService.queryTagListByParams(currentPage,pageSize,name);
    }

    @PostMapping("insertTag")
    public R insertCategory(@RequestBody TagEntity tagEntity){
        return tagService.insertTag(tagEntity);
    }

    @PostMapping("updateTag")
    public R updateCategory(@RequestBody TagEntity tagEntity){
        return tagService.updateTag(tagEntity);
    }

    @GetMapping("deleteTag")
    public R deleteCategory(@RequestParam Long id){
        return tagService.deleteTag(id);
    }


}
