package com.yfdl.controller;

import cn.hutool.core.util.StrUtil;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.dto.LoginDto;
import com.yfdl.dto.user.ChangePasswordDto;
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
import org.apache.catalina.User;
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
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BlogLoginService blogLoginService;

    @ApiOperation("获取登录用户的详细信息,需要登录")
    @GetMapping("/userInfo")
    public R userInfo(){
        return userService.userInfo();
    }

    @ApiOperation("修改登录用户的详细信息,需要登录")
    @PutMapping("/userInfo")
    public R userInfo(@RequestBody UserEntity user){
        return userService.updateUserInfo(user);
    }

    @ApiOperation("简单的注册")
    @PostMapping("/register")
    public R register(@RequestBody UserEntity user){
        return userService.register(user);
    }


    /**
     * 发送验证码
     */
    @ApiOperation("发送验证码，不用验证邮箱是否使用")
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam String email){
        return userService.sendCode(email);
    }

    @ApiOperation("发送验证码，需要验证码邮箱没有被使用过")
    @GetMapping("/sendCodeNeedVerify")
    public R sendCodeNeedVerify(@RequestParam String email){
        return userService.sendCodeNeedVerify(email);
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

    @ApiOperation("获取观看文章的作者信息")
    @GetMapping("/authorInfoByArticle")
    public R authorInfoByArticle(HttpServletRequest httpServletRequest, @RequestParam Long articleId){
        return userService.authorInfoByArticle(httpServletRequest,articleId);
    }

    @ApiOperation("获取用户得详细信息,主页展示")
    @GetMapping("/userInfoByHomePage")
    public R userInfoByHomePage(HttpServletRequest httpServletRequest, @RequestParam Long userId){
        return userService.userInfoByHomePage(httpServletRequest,userId);
    }

    @ApiOperation("更新用户信息,需要登录")
    @PostMapping("/updateUserInfo")
    public R updateUserInfo(@RequestBody UserEntity user){
        /**
         * 更新用户信息,不能直接设置用户，名手机号，设置密码
         */
        user.setEmail(null);
        user.setPhonenumber(null);
        user.setPassword(null);
        return userService.updateInfo(user);
    }

    @ApiOperation("绑定邮箱,需要登录")
    @GetMapping("/updateEmail")
    public R updateEmail(@RequestParam String email , @RequestParam String code){
        return userService.updateEmail(email,code);
    }

    @ApiOperation("解除邮箱绑定,需要登录")
    @GetMapping("/unbindingEmail")
    public R unbindingEmail(){
        return userService.unbindingEmail();
    }

    @ApiOperation("修改密码")
    @PostMapping("/changePassword")
    public R changePassword(@RequestBody ChangePasswordDto changePassword) {
        return userService.changePassword(changePassword);
    }
}
