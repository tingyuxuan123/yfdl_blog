package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.LinkService;
import com.yfdl.vo.LinkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("getAllLink")
    public R<List<LinkVo>> getAllLink(){
        //获取所有
        return linkService.getAllLink();
    }

}
