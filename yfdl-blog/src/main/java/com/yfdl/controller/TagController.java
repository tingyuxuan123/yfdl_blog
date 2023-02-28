package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("queryTagListByParams")
    public R allCategoryDetailList(@RequestParam(defaultValue = "1") Long currentPage,
                                   @RequestParam(defaultValue = "10") Long pageSize,
                                   @RequestParam(required = false) String name
    ) {

        return tagService.queryTagListByParams(currentPage,pageSize,name);
    }

    @GetMapping("tagInfoById")
    public R tagInfoById(@RequestParam Long tagId){
        return tagService.tagInfoById(tagId);
    }

}
