package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.dto.collect.InsertCollectDto;
import com.yfdl.service.CollectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    @PostMapping("insertCollect")
    public R insertCollect(@RequestBody InsertCollectDto insertCollectDto){
            return collectService.insertCollect(insertCollectDto);
    }

    @GetMapping("cancelCollect")
    public R cancelCollect(@RequestParam Long articleId){
        return collectService.cancelCollect(articleId);
    }
}
