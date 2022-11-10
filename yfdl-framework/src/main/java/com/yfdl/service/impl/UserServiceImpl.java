package com.yfdl.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.constants.SystemConstants;
import com.yfdl.dto.user.LoginOrRegisterByCodeDto;
import com.yfdl.dto.user.UserInfoByInsertDto;
import com.yfdl.dto.user.UserInfoByUpdateDto;
import com.yfdl.entity.*;
import com.yfdl.service.*;
import com.yfdl.mapper.UserMapper;
import com.yfdl.utils.*;
import com.yfdl.vo.*;
import com.yfdl.vo.user.AuthorInfoByArticleDto;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private ArticleServiceImpl articleService;

    @Resource
    private FollowService followService;


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

    @Value("${defaultAvatar}")
    private String defaultAvatar;

    @Value("${defaultNicknamePrefix}")
    private String defaultNicknamePrefix;

    @Override
    public R loginOrRegisterByCode(LoginOrRegisterByCodeDto loginOrRegisterByCodeDto) {
        String email=loginOrRegisterByCodeDto.getEmail();

        if (ObjectUtil.hasEmpty(loginOrRegisterByCodeDto)){ //如果对象中有值为空
            return R.errorResult(400,"参数错误");
        }

        UserEntity user = query().eq("email", loginOrRegisterByCodeDto.getEmail()).one();
        if (ObjectUtil.isNotEmpty(user)) {
            //不为空 登录
            String code = stringRedisTemplate.opsForValue().get(RedisConstant.BLOG_LOGIN_CODE + email);
            if(loginOrRegisterByCodeDto.getCode().equals(code)){
                //删除验证码
                stringRedisTemplate.delete(RedisConstant.BLOG_LOGIN_CODE + email);
                //验证码正确，登录成功

                //获取userid 生成token
                LoginUser loginUser = new LoginUser();
                loginUser.setUser(user);

                String userId=loginUser.getUser().getId().toString();
                String jwt = JwtUtil.createJWT(userId);

                //把用户存入redis
                redisCache.setCacheObject("bloglogin"+userId,loginUser);
                redisCache.expire("bloglogin"+userId,(60 * 60 *1000L)*24, TimeUnit.SECONDS);

                //把token 和userinfo封装 返回
                UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
                BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);

                //返回用户信息;
                return R.successResult(blogUserLoginVo);  //返回用户信息
            }else {
                return R.errorResult(400,"验证码错误");
            }


        }else {
            //为空注册
            UserEntity userEntity = new UserEntity();
            userEntity.setEmail(email);
            userEntity.setNickName(defaultNicknamePrefix+ RandomUtil.randomString(10));
            userEntity.setAvatar(defaultAvatar);
            userEntity.setPassword(passwordEncoder.encode(InitPassword));
            String[] split = email.split("@");

            userEntity.setUserName(split[0]);
            boolean save = save(userEntity);


            if(save){
                //获取userid 生成token
                LoginUser loginUser = new LoginUser();
                loginUser.setUser(userEntity);

                String userId=loginUser.getUser().getId().toString();
                String jwt = JwtUtil.createJWT(userId);

                //把用户存入redis
                redisCache.setCacheObject("bloglogin"+userId,loginUser);
                redisCache.expire("bloglogin"+userId,(60 * 60 *1000L)*24, TimeUnit.SECONDS);

                //把token 和userinfo封装 返回
                UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
                BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);

                //返回用户信息;
                return R.successResult(blogUserLoginVo);  //返回用户信息
            }else {
                return R.errorResult(400,"系统错误");
            }

        }


    }

    @Override
    public R authorInfoByArticle(HttpServletRequest httpServletRequest, Long articleId) {

        String token = httpServletRequest.getHeader("token");


        //根据文章id 获取用户信息

        //1.1 获取用户id
        ArticleEntity article = articleService.query().select("create_by").eq("id",articleId).one();

        //1.2 根据用户id 获取用户信息
        UserEntity user = query().eq("id", article.getCreateBy()).one();

        AuthorInfoByArticleDto authorInfoByArticle = baseMapper.getAuthorInfoByArticle(user.getId());
        authorInfoByArticle.setIsFollow(false);

        //1.3 判断 是否登录
        if(!Objects.isNull(token)){
            Claims claims;
            try {
                claims = JwtUtil.parseJWT(token);

                String id = claims.getSubject();
                long userId = Long.parseLong(id);

                //1.4 如果登录查找关注表中 是否有关注

                FollowEntity follow = followService.query().eq("user_id", userId).eq("follow_user_id", authorInfoByArticle.getId()).one();

                if (ObjectUtil.isNotNull(follow)){
                    //1.5 不为空
                    authorInfoByArticle.setIsFollow(true);
                }else {
                    authorInfoByArticle.setIsFollow(false);
                }

            } catch (Exception e) {
                //出现异常表示为未登录
                authorInfoByArticle.setIsFollow(false);
                return R.successResult(authorInfoByArticle);
            }

        }





        return R.successResult(authorInfoByArticle);
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
