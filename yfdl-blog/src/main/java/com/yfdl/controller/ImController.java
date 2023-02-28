package com.yfdl.controller;

import com.yfdl.common.R;
import com.yfdl.service.ImService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/im")
public class ImController {

    @Resource
    private ImService imService;

    /**
     * 需要登录
     */
    @GetMapping("chatUserList")
    public R chatUserList(){
        return imService.chatUserList();
    }

    @GetMapping("chatHistory")
    public R chatHistory(@RequestParam Long toId){
        return imService.chatHistory(toId);
    }

}
