package com.yfdl.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.AppHttpCodeEnum;
import com.yfdl.common.R;
import com.yfdl.common.SystemException;
import com.yfdl.constants.SystemConstants;
import com.yfdl.dto.user.ChangePasswordDto;
import com.yfdl.dto.user.LoginOrRegisterByCodeDto;
import com.yfdl.dto.user.UserInfoByInsertDto;
import com.yfdl.dto.user.UserInfoByUpdateDto;
import com.yfdl.entity.*;
import com.yfdl.service.*;
import com.yfdl.mapper.UserMapper;
import com.yfdl.utils.*;
import com.yfdl.vo.*;
import com.yfdl.vo.user.AuthorInfoByArticleDto;
import com.yfdl.vo.user.UserInfoByHomePageVo;
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
import java.util.Map;
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

    @Resource
    private CollectionService collectionService;

    @Resource
    private SendEmail sendEmail;

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
     * 更新当前登录的用户信息
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
//            String[] split = email.split("@");
            while (true){
                //随机生成一个12位账号
                Long aLong = accountGenerator();
                //查询该账号是否存在
                UserEntity oneUser = query().eq("user_name", aLong).one();
                if(ObjectUtil.isNull(oneUser)) {
                    //为空设置账号
                    userEntity.setUserName(aLong.toString());
                    break;
                }
                //反之循环重新生成
            }


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

    /**
     * 随机生成一个账号
     * @return
     */
    public Long accountGenerator(){
        char[] nums= {'0','1','2','3','4','5','6','7','8','9'};
        Long account=null;
        Long startAccount= 100000000000L;
        String endAccount="";
        for (int i = 0; i < 11; i++) {
            int i1 = RandomUtil.randomInt(10);
            endAccount+=nums[i1];
        }
        account = startAccount | Long.parseLong(endAccount);

        return account;
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

    @Override
    public R userInfoByHomePage(HttpServletRequest httpServletRequest, Long userId) {
        String token = httpServletRequest.getHeader("token");

        UserEntity user = lambdaQuery().eq(UserEntity::getId, userId).one();

        UserInfoByHomePageVo userInfoByHomePageVo = BeanCopyUtils.copyBean(user, UserInfoByHomePageVo.class);

        //设置关注数
        Integer count = followService.lambdaQuery().eq(FollowEntity::getUserId, userId).count();
        userInfoByHomePageVo.setFollowCount(count.longValue());

        //设置被关注数
        Integer count1 = followService.lambdaQuery().eq(FollowEntity::getFollowUserId, userId).count();
        userInfoByHomePageVo.setBeFollowCount(count1.longValue());

        //获取文章被点赞数
        QueryWrapper<ArticleEntity> articleEntityQueryWrapper = new QueryWrapper<>();
        articleEntityQueryWrapper.eq("create_time",userId)
                .select("IFNULL(sum(view_count),0) as readCount");

        Map<String, Object> map = articleService.getMap(articleEntityQueryWrapper);
        long readCount = Long.parseLong(String.valueOf(map.get("readCount")));
        userInfoByHomePageVo.setReadCount(readCount);

        //获取文章被阅读数
        QueryWrapper<ArticleEntity> articleEntityQueryWrapper1 = new QueryWrapper<>();
        articleEntityQueryWrapper1.eq("create_time",userId)
                .select("IFNULL(sum(likes_count),0) as likesCount");

        Map<String, Object> map1 = articleService.getMap(articleEntityQueryWrapper1);
        long likesCount = Long.parseLong(String.valueOf(map1.get("likesCount")));
        userInfoByHomePageVo.setLikesCount(likesCount);


        //获取用户创建的收藏夹数
        Integer count2 = collectionService.lambdaQuery().eq(CollectionEntity::getUserId, userId).count();
        userInfoByHomePageVo.setCollectionCount(count2.longValue());


        //判断是否关注
        //判断 是否登录
        if(!Objects.isNull(token)){
            Claims claims;
            try {
                claims = JwtUtil.parseJWT(token);

                String id = claims.getSubject();
                long loginId = Long.parseLong(id);

                //1.4 如果登录查找关注表中 是否有关注
                FollowEntity follow = followService.query().eq("user_id", loginId).eq("follow_user_id", userId).one();

                if (ObjectUtil.isNotNull(follow)){
                    //1.5 不为空
                    userInfoByHomePageVo.setIsFollow(true);
                }else {
                    userInfoByHomePageVo.setIsFollow(false);
                }

            } catch (Exception e) {
                //出现异常表示为未登录
                userInfoByHomePageVo.setIsFollow(false);
                return R.successResult(userInfoByHomePageVo);
            }

        }

        return R.successResult(userInfoByHomePageVo);
    }

    @Override
    public R updateEmail(String email, String code) {

        String redisCode = stringRedisTemplate.opsForValue().get(RedisConstant.BLOG_LOGIN_CODE + email);

        //有一个为空就返回验证失败
        if(ObjectUtil.isNull(code) || ObjectUtil.isNull(redisCode) || !ObjectUtil.equal(code,redisCode)){
           return R.errorResult(400,"验证码错误");
        }

        Long userId = SecurityUtils.getUserId();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail(email);
        boolean b = updateById(userEntity);
        if(b){
            return R.successResult();
        }else {
           return R.errorResult(400,"邮箱修改失败，请重试!");
        }
    }

    @Override
    public R unbindingEmail() {
        Long userId = SecurityUtils.getUserId();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("");
        boolean b = updateById(userEntity);
        if(b){
            return R.successResult();
        }else {
            return R.errorResult(400,"出现错误");
        }


    }

    @Override
    public R sendCode(String email) {
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

    @Override
    public R sendCodeNeedVerify(String email) {
        //查询该email是否已经被使用
        UserEntity user = lambdaQuery().eq(UserEntity::getEmail, email).one();
        if(ObjectUtil.isNotNull(user)){
            return R.errorResult(400,"该邮箱已被使用!");
        }
        //没有被使用过就发送验证码
        return sendCode(email);
    }

    @Override
    public R changePassword(ChangePasswordDto changePassword) {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        //验证用户和上传的密码是否正确
//        String cpwd = passwordEncoder.encode(changePassword.getPassword());//


        boolean isSame = passwordEncoder.matches(changePassword.getPassword(),loginUser.getUser().getPassword());
        //如果相同改变密码
        if(isSame){
            UserEntity userEntity = new UserEntity();
            userEntity.setId(loginUser.getUser().getId());
            userEntity.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
            boolean b = updateById(userEntity);
            return R.successResult("密码修改成功");
        }else{
            //密码不正确
            return R.errorResult(400,"密码不正确,请重试!");
        }

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
