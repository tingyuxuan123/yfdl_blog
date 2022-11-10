package com.yfdl.controller;

import cn.hutool.core.util.StrUtil;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.dto.LoginDto;
import com.yfdl.dto.user.LoginOrRegisterByCodeDto;
import com.yfdl.entity.UserEntity;
import com.yfdl.service.BlogLoginService;
import com.yfdl.service.UserService;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.utils.RedisConstant;
import com.yfdl.utils.SendEmail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "用户")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private SendEmail sendEmail;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BlogLoginService blogLoginService;

    @GetMapping("/userInfo")
    public R userInfo(){
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    public R userInfo(@RequestBody UserEntity user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    public R register(@RequestBody UserEntity user){
        return userService.register(user);
    }


    /**
     * 发送验证码
     */
    @ApiOperation("发送验证码")
    @GetMapping("/sendCode")
    public R login(@RequestParam String email){
        try{
            String code = stringRedisTemplate.opsForValue().get(RedisConstant.BLOG_LOGIN_CODE + email);

            if (!StrUtil.isEmpty(code)) {
                return R.errorResult(400,"获取验证码过于频繁");
            }

            sendEmail.sendEmail(email);
        }catch(Exception e){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        return R.successResult("验证码发送成功！");
    }


    /**
     * 用户登录
     * @param  user
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R login(@RequestBody UserEntity user){

        return blogLoginService.login(user);
    }

    @ApiOperation("根据验证码登录或注册")
    @PostMapping("/loginOrRegisterByCode")
    public R loginOrRegisterByCode(@RequestBody LoginOrRegisterByCodeDto loginOrRegisterByCodeDto){
        return  userService.loginOrRegisterByCode(loginOrRegisterByCodeDto);
    }

    @ApiOperation("退出登录")
    @GetMapping("/logout")
    public R logout(){
        return blogLoginService.logout();
    }

    @ApiOperation("获取说看文章的作者信息")
    @GetMapping("/authorInfoByArticle")
    public R authorInfoByArticle(HttpServletRequest httpServletRequest, @RequestParam Long articleId){
        return userService.authorInfoByArticle(httpServletRequest,articleId);
    }

}
