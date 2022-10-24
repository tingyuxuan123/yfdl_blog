package com.yfdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.constants.SystemConstants;
import com.yfdl.dto.user.UserInfoByInsertDto;
import com.yfdl.dto.user.UserInfoByUpdateDto;
import com.yfdl.entity.RoleMenuEntity;
import com.yfdl.entity.UserRoleEntity;
import com.yfdl.service.MenuService;
import com.yfdl.service.RoleService;
import com.yfdl.service.UserRoleService;
import com.yfdl.service.UserService;
import com.yfdl.mapper.UserMapper;
import com.yfdl.entity.UserEntity;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.utils.SecurityUtils;
import com.yfdl.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-09-25 19:28:22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public R userInfo() {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //获取用户信息
        UserEntity user = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return R.successResult(userInfoVo);
    }

    @Override
    public R updateUserInfo(UserEntity user) {
        boolean b = updateById(user);
        return R.successResult();

    }

    /**
     * 添加/注册用户信息
     * @param user
     * @return
     */
    @Transactional
    @Override
    public R register(UserEntity user) {
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }

        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }

        //对密码进行加密

        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        save(user);  //添加用户

        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setUserId(user.getId());
        userRoleEntity.setRoleId(SystemConstants.USER_ROLE_COMMON); //

        userRoleService.save(userRoleEntity);


        return R.successResult();
    }


    @Override
    public R getInfo() {
        //获取用户基本信息，权限，可以操作的功能
        //获取用户id
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserEntity user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //存入用户信息
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo();
        adminUserInfoVo.setUser(userInfoVo);
        List<String> perms = menuService.selectPermsByUserId(SecurityUtils.getUserId());

        List<String> roleKeyList= roleService.selectRoleKeyByUserId(SecurityUtils.getUserId());

        adminUserInfoVo.setPermissions(perms);
        adminUserInfoVo.setRoles(roleKeyList);
        return R.successResult(adminUserInfoVo);

//        Long userId = SecurityUtils.getUserId();
//        UserEntity user = getById(userId);
//        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

      //  return R.successResult(userInfoVo);

    }

    @Override
    public R getRouters() {
        //获取该用户能看到的菜单和分类
         Long userId = SecurityUtils.getUserId();
         List<MenuVo> menus= menuService.selectRouterMenuTreeByUserId(userId);
        return R.successResult(new RouterVo(menus));
    }

    /**
     * 更新当前信息
     * @param user
     * @return
     */
    @Override
    public R updateInfo(UserEntity user) {
        Long userId = SecurityUtils.getUserId();
        user.setId(userId);
        boolean b = updateById(user);

        return getInfo();
    }



    @Override
    public R updatePassword(UserEntity user) {
        Long userId = SecurityUtils.getUserId();
        user.setId(userId);
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        updateById(user);

        return R.successResult();
    }

    @Override
    public R userList(Long currentPage, Long pageSize, String userName, String phonenumber, String status) {

        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(userName),UserEntity::getUserName,userName);
        queryWrapper.eq(Objects.nonNull(phonenumber),UserEntity::getPhonenumber,phonenumber);
        queryWrapper.eq(Objects.nonNull(status),UserEntity::getStatus,status);
        Page<UserEntity> userEntityPage = new Page<>(currentPage,pageSize);
        page(userEntityPage,queryWrapper);

        List<AdminUserListVo> adminUserListVos = BeanCopyUtils.copyBeanList(userEntityPage.getRecords(), AdminUserListVo.class);

        //获取对应的权限id
        adminUserListVos.stream().forEach(adminUserListVo -> {
            Long id = adminUserListVo.getId();
            LambdaQueryWrapper<UserRoleEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(UserRoleEntity::getUserId,id);
            UserRoleEntity user = userRoleService.getOne(lambdaQueryWrapper);
            if(Objects.nonNull(user)){
                Long roleId = user.getRoleId();
                adminUserListVo.setRoleId(roleId);
            }

        });

        PageVo<AdminUserListVo> adminUserListVoPageVo = new PageVo<>(adminUserListVos, userEntityPage.getTotal());

//        System.out.println(adminUserListVoPageVo);

        return R.successResult(adminUserListVoPageVo);
    }

    @Transactional
    @Override
    public R AdminupdateUserInfo(UserInfoByUpdateDto userInfoByUpdateDto) {
        UserEntity userEntity = BeanCopyUtils.copyBean(userInfoByUpdateDto, UserEntity.class);
        boolean b = updateById(userEntity);

        if (Objects.nonNull(userInfoByUpdateDto.getRoleId())){
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userEntity.getId());
            userRoleEntity.setRoleId(userInfoByUpdateDto.getRoleId());
            LambdaQueryWrapper<UserRoleEntity> userRoleEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userRoleEntityLambdaQueryWrapper.eq(UserRoleEntity::getUserId,userEntity.getId());

            boolean update = userRoleService.update(userRoleEntity, userRoleEntityLambdaQueryWrapper);

        }

        return R.successResult();
    }

    @Value("${InitPassword}")
    private String InitPassword;
    /**
     * 用户管理添加用户
     * @param userInfoByInsertDto
     * @return
     */
    @Transactional
    @Override
    public R insertUser(UserInfoByInsertDto userInfoByInsertDto) {
        UserEntity userEntity = BeanCopyUtils.copyBean(userInfoByInsertDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(InitPassword));
        boolean isSave = save(userEntity);

        if(Objects.nonNull(userInfoByInsertDto.getRoleId())){
            UserRoleEntity userRoleEntity = new UserRoleEntity();
            userRoleEntity.setUserId(userEntity.getId());
            userRoleEntity.setRoleId(userInfoByInsertDto.getRoleId());
            boolean update = userRoleService.save(userRoleEntity);

        }

        return R.successResult();
    }

    public boolean userNameExist(String username){
        LambdaQueryWrapper<UserEntity> userEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userEntityLambdaQueryWrapper.eq(UserEntity::getUserName,username);
        List<UserEntity> list = list(userEntityLambdaQueryWrapper);
        if (list.size()>=1){
            return true;
        }else {
            return false;
        }

    }

    public boolean nickNameExist(String nickname){
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getNickName,nickname);
        List<UserEntity> list = list(queryWrapper);
        if (list.size()>=1){
            return true;
        }else {
            return false;
        }
    }


}
