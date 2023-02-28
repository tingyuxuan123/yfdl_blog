package com.yfdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfdl.common.R;
import com.yfdl.dto.user.ChangePasswordDto;
import com.yfdl.dto.user.LoginOrRegisterByCodeDto;
import com.yfdl.dto.user.UserInfoByInsertDto;
import com.yfdl.dto.user.UserInfoByUpdateDto;
import com.yfdl.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-09-25 19:28:22
 */
public interface UserService extends IService<UserEntity> {
    R userInfo();

    R updateUserInfo(UserEntity user);

    R register(UserEntity user);


    R getInfo();

    R getRouters();

    R updateInfo(UserEntity user);

    R updatePassword(UserEntity user);

    R userList(Long currentPage, Long pageSize, String userName, String phonenumber, String status);

    R AdminupdateUserInfo(UserInfoByUpdateDto userInfoByUpdateDto);

    R insertUser(UserInfoByInsertDto userInfoByInsertDto);

    R loginOrRegisterByCode(LoginOrRegisterByCodeDto loginOrRegisterByCodeDto);

    R authorInfoByArticle(HttpServletRequest httpServletRequest, Long articleId);

    R userInfoByHomePage(HttpServletRequest httpServletRequest, Long userId);

    R updateEmail(String email, String code);


    R unbindingEmail();

    R sendCode(String email);

    R sendCodeNeedVerify(String email);

    R changePassword(ChangePasswordDto changePassword);

    R searchUser(Long currentPage, Long pageSize, String searchParams);

    R userArticleInfo();
}
