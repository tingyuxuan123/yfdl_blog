package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.entity.CollectionEntity;
import com.yfdl.service.CollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("收藏夹")
@RestController
@RequestMapping("/collection")
public class CollectionController {


    @Resource
    private CollectionService collectionService;

    @ApiOperation("获取收藏夹列表,需要登录")
    @GetMapping("collectionList")
    public R collectionList(){
        return collectionService.collectionList();
    }

    @ApiOperation("创建收藏夹")
    @PostMapping("createCollection")
    public R createCollection(@RequestBody CollectionEntity collectionEntity){
        return collectionService.createCollection(collectionEntity);
    }

    @ApiOperation("修改收藏夹")
    @PostMapping("updateCollection")
    public R updateCollection(@RequestBody CollectionEntity collectionEntity){
        return collectionService.updateCollection(collectionEntity);
    }

    @ApiOperation("删除收藏夹")
    @GetMapping("deleteCollection")
    public R deleteCollection(@RequestParam Long collectionId){
        return collectionService.deleteCollection(collectionId);
    }
}
