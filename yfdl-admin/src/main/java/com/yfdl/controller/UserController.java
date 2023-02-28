package com.yfdl.controller;

import cn.hutool.core.util.StrUtil;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.dto.LoginDto;
import com.yfdl.dto.user.LoginOrRegisterByCodeDto;
import com.yfdl.dto.user.UserInfoByInsertDto;
import com.yfdl.dto.user.UserInfoByUpdateDto;
import com.yfdl.entity.UserEntity;
import com.yfdl.service.AdminLoginService;
import com.yfdl.service.UserService;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.utils.RedisConstant;
import com.yfdl.utils.SendEmail;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminLoginService adminLoginService;

    @Resource
    private SendEmail sendEmail;

    @Resource
    private StringRedisTemplate stringRedisTemplate;




    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    @PostMapping("/user/login")
    public R login(@RequestBody LoginDto loginDto){
        UserEntity userEntity = BeanCopyUtils.copyBean(loginDto, UserEntity.class);
        return adminLoginService.login(userEntity);
    }

    @ApiOperation("发送验证码，不用验证邮箱是否使用")
    @GetMapping("/user/sendCode")
    public R sendCode(@RequestParam String email){
        return userService.sendCode(email);
    }


    @ApiOperation("根据验证码登录或注册")
    @PostMapping("/user/loginOrRegisterByCode")
    public R loginOrRegisterByCode(@RequestBody LoginOrRegisterByCodeDto loginOrRegisterByCodeDto){
        return  userService.loginOrRegisterByCode(loginOrRegisterByCodeDto);
    }

    /**
     * 注册,默认权限为普通用户
     * @return
     */
    @PostMapping("/user/register")
    public R register(@RequestBody UserEntity user){
        return userService.register(user);
    }

    @PostMapping("/user/insertUser")
    public R insertUser(@RequestBody UserInfoByInsertDto userInfoByInsertDto){
        return userService.insertUser(userInfoByInsertDto);
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/getInfo")
    public R getInfo(){
        return userService.getInfo();
    }


    @GetMapping("/getRouters")
    public R getRouters(){
        return userService.getRouters();
    }

    /**
     * 更新个人信息
     * 需要登录
     */
    @PostMapping("user/updateInfo")
    public R updateInfo(@RequestBody UserEntity user){
        return userService.updateInfo(user);
    }

    /**
     * 修改用户信息
     * @return
     */
    @PostMapping("user/updateUserInfo")
    public R AdminUpdateUserInfo(@RequestBody UserInfoByUpdateDto userInfoByUpdateDto){
        return userService.AdminupdateUserInfo(userInfoByUpdateDto);
    }

    /**
     * 重设密码需要登录
     * @param user
     * @return
     */
    @PostMapping("user/updatePassword")
    public R updatePassword(@RequestBody UserEntity user){
        return userService.updatePassword(user);
    }


    @GetMapping("/user/userList")
    public R userList(@RequestParam Long currentPage,
                      @RequestParam Long pageSize,
                      @RequestParam(required = false) String userName,
                      @RequestParam(required = false) String phonenumber,
                      @RequestParam(required = false) String status ){

        return  userService.userList(currentPage,pageSize,userName,phonenumber,status);
    }

    @ApiOperation("获取用户文章信息,需登录")
    @GetMapping("/user/userArticleInfo")
    public R userArticleInfo(){
        return userService.userArticleInfo();
    }

}
