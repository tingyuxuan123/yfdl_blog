package com.yfdl.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.constants.SystemConstants;
import com.yfdl.dto.ArticleDetailDto;
import com.yfdl.entity.*;
import com.yfdl.service.*;
import com.yfdl.mapper.ArticleMapper;
import com.yfdl.utils.BeanCopyUtils;
import com.yfdl.utils.JwtUtil;
import com.yfdl.utils.RedisCache;
import com.yfdl.utils.SecurityUtils;
import com.yfdl.vo.*;
import com.yfdl.vo.article.ArticleAudit;
import com.yfdl.vo.collection.CollectionInfoVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-09-23 23:48:37
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleEntity> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Resource
    private LikesService likesService;

    @Resource
    private CollectService collectService;

    @Resource
    private CollectionService collectionService;

    @Override
    public R<List<ArticleEntity>> hotArticleList() {

        LambdaQueryWrapper<ArticleEntity> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ArticleEntity::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.eq(ArticleEntity::getAudit ,0); //要求过审
        queryWrapper.orderByDesc(ArticleEntity::getViewCount); //按照观看量从大到小排序
        Page<ArticleEntity> articleEntityPage = new Page<>(1, 10);
        Page<ArticleEntity> page = page(articleEntityPage, queryWrapper);

        List<ArticleEntity> articles=page.getRecords();

        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return R.successResult(hotArticleVos);
    }

    /**
     * 查询文章列表(前台)
     *
     * @param title       #文章标题
     * @param status      #文章状态
     * @param categoryId  #文章分类
     * @param spanId      #文章标签id
     * @param currentPage
     * @param pageSize
     * @param userId     #文章的创作者
     * @return
     */
    @Override
    public R<PageVo<ArticleListVo>> articleList(String title, Character status, Long categoryId, Long spanId, Long userId, Long currentPage, Long pageSize) {

        LambdaQueryWrapper<ArticleEntity> queryWrapper = new LambdaQueryWrapper<>();
        if(Objects.nonNull(status) && (status=='0' || status=='1')){  //状态不为空按传输的值搜索
            queryWrapper.eq(ArticleEntity::getStatus,status);
        }else{
            queryWrapper.eq(ArticleEntity::getStatus,'0');
        }

        //只查询通过审核的文章
        queryWrapper.eq(ArticleEntity::getAudit,0);

        //如果标题存在根据标题查询
        queryWrapper.like(Objects.nonNull(title),ArticleEntity::getTitle,title);

        //如果用户id存在根据用户id查询
        queryWrapper.eq(Objects.nonNull(userId),ArticleEntity::getCreateBy,userId);

        if(Objects.nonNull(spanId) && spanId>0){
            LambdaQueryWrapper<ArticleTagEntity> articleTagEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<ArticleTagEntity> eq = articleTagEntityLambdaQueryWrapper.eq(ArticleTagEntity::getTagId,spanId);
            List<Long> articleIds = articleTagService.list(eq).stream().map(ArticleTagEntity::getArticleId).collect(Collectors.toList());
            queryWrapper.in(ArticleEntity::getId,articleIds);
        }


        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,ArticleEntity::getCategoryId,categoryId);
        queryWrapper.orderByDesc(ArticleEntity::getIsTop,ArticleEntity::getCreateTime);
        Page<ArticleEntity> articleEntityPage = new Page<>(currentPage,pageSize);
        page(articleEntityPage,queryWrapper); //查询结果会存入articleEntityPage

        List<ArticleEntity> articleEntities = articleEntityPage.getRecords();

        //获取分类id对应的分类名称
         articleEntities= articleEntities.stream().peek(articleEntity -> {
            CategoryEntity categoryEntity = categoryService.getById(articleEntity.getCategoryId());
            String name = categoryEntity.getName();
            articleEntity.setCategoryName(name);
            //获取观看量
             Integer viewCount = redisCache.getCacheMapValue("article:viewCount", articleEntity.getId().toString());
             if(Objects.nonNull(viewCount)){ //如果redis中读取到观看数就设置反之就使用数据库中的
                 articleEntity.setViewCount(viewCount.longValue());
             }

             //获取评论数
             LambdaQueryWrapper<CommentEntity> commentEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
             commentEntityLambdaQueryWrapper.eq(CommentEntity::getArticleId,articleEntity.getId());
             int count = commentService.count(commentEntityLambdaQueryWrapper);
             articleEntity.setCommentCount((long) count);

        }).collect(Collectors.toList());

        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleEntities, ArticleListVo.class);


        articleListVos.forEach(this::setTag);
        articleListVos.forEach(this::setNickNameAndAvatar);
        articleListVos.forEach(this::setCommentCount);

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos, articleEntityPage.getTotal());



        return R.successResult(articleListVoPageVo);
    }


    /**
     * 给文章列表添加用户名和头像
     * @param articleListVo
     */
    private void setNickNameAndAvatar(ArticleListVo articleListVo){

        if(Objects.nonNull(articleListVo.getCreateBy())){
            UserEntity user = userService.getById(articleListVo.getCreateBy());
            if(Objects.nonNull(user.getAvatar())){
                articleListVo.setAvatar(user.getAvatar());
            }
            if(Objects.nonNull(user.getNickName())){
                articleListVo.setNickName(user.getNickName());
            }
        }
    }

    private void setCommentCount(ArticleListVo articleListVo){
        Integer count = commentService.query()
                .eq("article_id", articleListVo.getId())
                .eq("del_flag",0)
                .in("audit",0,1).count();


        articleListVo.setCommentCount(count);
    }

    private void setTag(ArticleListVo articleListVo){ //设置对应文章的标签列表
        LambdaQueryWrapper<ArticleTagEntity> articleTagEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据文章id,获取标签id
        articleTagEntityLambdaQueryWrapper.eq(ArticleTagEntity::getArticleId,articleListVo.getId());
        List<Long> tagIds = articleTagService.list(articleTagEntityLambdaQueryWrapper).stream()
                .map(articleTagEntity -> articleTagEntity.getTagId()).collect(Collectors.toList());
        System.out.println(tagIds);
        LambdaQueryWrapper<TagEntity> tagEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagEntityLambdaQueryWrapper.in(tagIds.size()>0,TagEntity::getId,tagIds);
        List<TagEntity> list = tagService.list(tagEntityLambdaQueryWrapper);
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(list, TagListVo.class);
        articleListVo.setTags(tagListVos);

    }

    private void setTag(ArticleVo articleVo){ //设置对应文章的标签列表
        LambdaQueryWrapper<ArticleTagEntity> articleTagEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据文章id,获取标签id
        articleTagEntityLambdaQueryWrapper.eq(ArticleTagEntity::getArticleId,articleVo.getId());
        List<Long> tagIds = articleTagService.list(articleTagEntityLambdaQueryWrapper).stream()
                .map(articleTagEntity -> articleTagEntity.getTagId()).collect(Collectors.toList());
        System.out.println(tagIds);
        LambdaQueryWrapper<TagEntity> tagEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagEntityLambdaQueryWrapper.in(tagIds.size()>0,TagEntity::getId,tagIds);
        List<TagEntity> list = tagService.list(tagEntityLambdaQueryWrapper);
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(list, TagListVo.class);
        articleVo.setTags(tagListVos);

    }



    @Override
    public R<ArticleVo> article(HttpServletRequest httpServletRequest, Long id) {

        String token = httpServletRequest.getHeader("token");

        ArticleEntity articleEntity = getById(id);

        CategoryEntity category = categoryService.getById(articleEntity.getCategoryId());
        articleEntity.setCategoryName(category.getName());



        ArticleVo articleVo = BeanCopyUtils.copyBean(articleEntity, ArticleVo.class);
        setTag(articleVo);

        if(Objects.nonNull(articleVo.getCreateBy())){
            UserEntity user = userService.getById(articleVo.getCreateBy());
            if(Objects.nonNull(user.getAvatar())){
                articleVo.setAvatar(user.getAvatar());
            }
            if(Objects.nonNull(user.getNickName())){
                articleVo.setNickName(user.getNickName());
            }
        }

        //获取评论数
        Integer count = commentService.query().eq("article_id", id).count();
        articleVo.setCommentCount((long)count);


        articleVo.setIsLikes(false);
        //判断是否登录
        if(!Objects.isNull(token)){
            Claims claims;
            try {
                claims = JwtUtil.parseJWT(token);

                String userid = claims.getSubject();
                long userId = Long.parseLong(userid);


                //判断是否点赞
                LikesEntity likes = likesService.query().eq("like_article_id", id).eq("user_id",userId).one();
                if(ObjectUtil.isNotNull(likes)){
                    articleVo.setIsLikes(true);
                }

                //判断是否收藏

                CollectEntity one = collectService.query().eq("create_by", userId).eq("article_id", id).one();
                if (ObjectUtil.isNotNull(one)) {
                    articleVo.setIsCollect(true);
                }


            } catch (Exception e) {
                //出现异常表示为未登录
            }

        }




        return R.successResult(articleVo);
    }

    @Override
    public R updateViewCount(Long id) {
        //浏览量加1
    /*    redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);*/
        boolean b = update().eq("id", id).setSql("view_count=view_count+1").update();


        return R.successResult();
    }

    @Transactional
    @Override
    public R updateArticle(@RequestBody ArticleDetailDto articleDetailDto) {

        List<Long> tags = articleDetailDto.getTags();


        ArticleEntity articleEntity = BeanCopyUtils.copyBean(articleDetailDto, ArticleEntity.class);

        if(Objects.nonNull(articleDetailDto.getTags()) && articleDetailDto.getTags().size()>0){
            //移除过时的标签
            LambdaQueryWrapper<ArticleTagEntity> articleTagEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagEntityLambdaQueryWrapper.eq(ArticleTagEntity::getArticleId,articleDetailDto.getId());
            articleTagService.remove(articleTagEntityLambdaQueryWrapper);
            //更新标签
            tags.stream().forEach(tag -> {
                ArticleTagEntity articleTagEntity = new ArticleTagEntity();
                articleTagEntity.setArticleId(articleDetailDto.getId());
                articleTagEntity.setTagId(tag);
                articleTagService.save(articleTagEntity);
            });
        }

        //把审核状态变为待审核，需要重新审核
        articleEntity.setAudit(1);

        boolean b = updateById(articleEntity);
        return R.successResult();
    }

    @Transactional
    @Override
    public R insertArticle(@RequestBody ArticleDetailDto articleDetailDto) {

        ArticleEntity articleEntity = BeanCopyUtils.copyBean(articleDetailDto, ArticleEntity.class);
        //保存对应的标签
        List<Long> tags = articleDetailDto.getTags();
        //保存文章
        save(articleEntity);

        tags.stream().forEach(tag -> {
            ArticleTagEntity articleTagEntity = new ArticleTagEntity();
            articleTagEntity.setArticleId(articleEntity.getId());
            articleTagEntity.setTagId(tag);
            articleTagService.save(articleTagEntity);
        });



        return R.successResult();
    }

    /**
     * 后台用户对个人的文章
     * @param title
     * @param status
     * @param categoryId
     * @param spanId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public R<PageVo<ArticleListVo>> adminArticleList(String title, Character status, Long categoryId, Long spanId, Long currentPage, Long pageSize) {

        //1.获取id 查看权限 返回对应的文章
        //当前查询人的账号
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<UserRoleEntity> userRoleEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleEntityLambdaQueryWrapper.eq(UserRoleEntity::getUserId,userId);
        UserRoleEntity userRole = userRoleService.getOne(userRoleEntityLambdaQueryWrapper);


        LambdaQueryWrapper<ArticleEntity> queryWrapper = new LambdaQueryWrapper<>();

        //根据权限显示对应的数据
        queryWrapper.eq(ArticleEntity::getCreateBy,userId);


        if(Objects.nonNull(status) && (status=='0' || status=='1')){  //状态不为空按传输的值搜索
            queryWrapper.eq(ArticleEntity::getStatus,status);
        }
        queryWrapper.like(Objects.nonNull(title),ArticleEntity::getTitle,title);

        if(Objects.nonNull(spanId) && spanId>0){
            LambdaQueryWrapper<ArticleTagEntity> articleTagEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<ArticleTagEntity> eq = articleTagEntityLambdaQueryWrapper.eq(ArticleTagEntity::getTagId,spanId);
            List<Long> articleIds = articleTagService.list(eq).stream().map(ArticleTagEntity::getArticleId).collect(Collectors.toList());
            queryWrapper.in(ArticleEntity::getId,articleIds);
        }


        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,ArticleEntity::getCategoryId,categoryId);
        queryWrapper.orderByDesc(ArticleEntity::getIsTop);
        Page<ArticleEntity> articleEntityPage = new Page<>(currentPage,pageSize);
        page(articleEntityPage,queryWrapper); //查询结果会存入articleEntityPage

        List<ArticleEntity> articleEntities = articleEntityPage.getRecords();

        //获取分类id对应的分类名称
        articleEntities= articleEntities.stream().peek(articleEntity -> {
            CategoryEntity categoryEntity = categoryService.getById(articleEntity.getCategoryId());
            String name = categoryEntity.getName();
            articleEntity.setCategoryName(name);
            //获取观看量
            Integer viewCount = redisCache.getCacheMapValue("article:viewCount", articleEntity.getId().toString());
            if(Objects.nonNull(viewCount)){ //如果redis中读取到观看数就设置反之就使用数据库中的
                articleEntity.setViewCount(viewCount.longValue());
            }

            //获取评论数
            LambdaQueryWrapper<CommentEntity> commentEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentEntityLambdaQueryWrapper.eq(CommentEntity::getArticleId,articleEntity.getId());
            int count = commentService.count(commentEntityLambdaQueryWrapper);
            articleEntity.setCommentCount((long) count);


        }).collect(Collectors.toList());


        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articleEntities, ArticleListVo.class);

        articleListVos.stream().forEach(this::setTag);
        articleListVos.stream().forEach(this::setCommentCount);

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos, articleEntityPage.getTotal());

        return R.successResult(articleListVoPageVo);
    }




    /**
     * 弃用
     */
    @Override
    public R<PageVo<ArticleListVo>> articleListByUserId(ArticleEntity articleEntity, Long currentPage, Long pageSize) {


        Page<ArticleEntity> articleEntityPage = new Page<>(currentPage,pageSize);

        //设置查询条件
        LambdaQueryWrapper<ArticleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(articleEntity.getCreateBy()),ArticleEntity::getCreateBy,articleEntity.getCreateBy());

        //进行分页查询
        page(articleEntityPage,queryWrapper);

        List<ArticleEntity> articles = articleEntityPage.getRecords();

        //对查询的数据进行处理
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos, articleEntityPage.getTotal());

        return R.successResult(articleListVoPageVo);
    }

    @Override
    public R articleListByUserLikes(Long userId) {
       ArticleListVo[] articleListVos= baseMapper.articleListByUserLikes(userId);

       Arrays.stream(articleListVos).forEach(this::setTag);


        return R.successResult(articleListVos);
    }

    @Override
    public R articleListByRecommended(Long currentPage, Long pageSize) {

        Long skipNumber =(currentPage-1) * pageSize;

        //获取推荐文章列表
        ArticleListVo[] articleListVos= baseMapper.articleListByRecommended(skipNumber,pageSize);
        //获取复合条件的总数
        Long total= baseMapper.getCount();
        Arrays.stream(articleListVos).forEach(this::setTag);
        Arrays.stream(articleListVos).forEach(this::setCommentCount);

        //转成list
        List<ArticleListVo> articleListVos1 = Arrays.stream(articleListVos).collect(Collectors.toList());

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos1,total);

        return R.successResult(articleListVoPageVo);
    }

    @Override
    public R articleListByNew(Long currentPage, Long pageSize) {
        Long skipNumber =(currentPage-1) * pageSize;
        ArticleListVo[] articleListVos= baseMapper.articleListByNew(skipNumber,pageSize);
        //获取复合条件的总数
        Long total= baseMapper.getCount();
        Arrays.stream(articleListVos).forEach(this::setTag);
        Arrays.stream(articleListVos).forEach(this::setCommentCount);

        //转成list
        List<ArticleListVo> articleListVos1 = Arrays.stream(articleListVos).collect(Collectors.toList());

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos1,total);

        return R.successResult(articleListVoPageVo);
    }


    @Override
    public R articleListByDynamic(Long currentPage, Long pageSize) {
        Long skipNumber =(currentPage-1) * pageSize;

        Long userId = SecurityUtils.getUserId();

        ArticleListVo[] articleListVos= baseMapper.articleListByDynamic(skipNumber,pageSize,userId);
        //获取复合条件的总数
        Long total= baseMapper.getCount();

        Arrays.stream(articleListVos).forEach(this::setTag);
        Arrays.stream(articleListVos).forEach(this::setCommentCount);


        //转成list
        List<ArticleListVo> articleListVos1 = Arrays.stream(articleListVos).collect(Collectors.toList());

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos1,total);

        return R.successResult(articleListVoPageVo);
    }

    @Override
    public R articleListByTag(Long spanId, Long currentPage, Long pageSize) {

        Long skipNumber =(currentPage-1) * pageSize;


        ArticleListVo[] articleListVos= baseMapper.articleListByTag(skipNumber,pageSize,spanId);
        //获取复合条件的总数
        Long total= baseMapper.getCount();

        Arrays.stream(articleListVos).forEach(this::setTag);
        Arrays.stream(articleListVos).forEach(this::setCommentCount);


        //转成list
        List<ArticleListVo> articleListVos1 = Arrays.stream(articleListVos).collect(Collectors.toList());

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos1,total);

        return R.successResult(articleListVoPageVo);

    }

    /**
     *  文章管理的文章列表
     * @param title
     * @param categoryId
     * @param spanId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public R adminAllArticleList(String title, Long categoryId, Long spanId, Long currentPage, Long pageSize) {

        Long skipNumber =(currentPage-1) * pageSize;

        //获取推荐文章列表
        ArticleListVo[] articleListVos= baseMapper.adminAllArticleList(skipNumber,pageSize,title,categoryId,spanId);
        //获取复合条件的总数
        Long total= baseMapper.getCount();
        Arrays.stream(articleListVos).forEach(this::setTag);
        Arrays.stream(articleListVos).forEach(this::setCommentCount);

        //转成list
        List<ArticleListVo> articleListVos1 = Arrays.stream(articleListVos).collect(Collectors.toList());

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos1,total);

        return R.successResult(articleListVoPageVo);

    }

    @Override
    @Transactional
    public R articleListByCollection(Long currentPage, Long pageSize, Long collectionId) {
        Long skipNumber =(currentPage-1) * pageSize;
        //根据收藏夹id获取推荐文章列表
        ArticleListVo[] articleListVos= baseMapper.articleListByCollection(skipNumber,pageSize,collectionId);
        //获取复合条件的总数
        Long total= baseMapper.getCount();
        Arrays.stream(articleListVos).forEach(this::setTag);
        Arrays.stream(articleListVos).forEach(this::setCommentCount);

        //转成list
        List<ArticleListVo> articleListVos1 = Arrays.stream(articleListVos).collect(Collectors.toList());
        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos1,total);

        CollectionEntity collection = collectionService.query().eq("id", collectionId).one();

        CollectionInfoVo collectionInfoVo =new CollectionInfoVo();
        collectionInfoVo.setCollection(collection);
        collectionInfoVo.setArticleListVo(articleListVoPageVo);

        return R.successResult(collectionInfoVo);
    }

    @Override
    public R articleListByRelated(String articleTitle) {

        ArticleListVo[] articleListVos= baseMapper.articleListByRelated(articleTitle);
        Arrays.stream(articleListVos).forEach(this::setTag);
        Arrays.stream(articleListVos).forEach(this::setCommentCount);
        return R.successResult(articleListVos);
    }

    @Override
    public R articleListByCategoryId(Long currentPage, Long pageSize, Long categoryId) {
        Long skipNumber =(currentPage-1) * pageSize;


        ArticleListVo[] articleListVos= baseMapper.articleListByCategoryId(skipNumber,pageSize,categoryId);
        //获取复合条件的总数
        Long total= baseMapper.getCount();

        Arrays.stream(articleListVos).forEach(this::setTag);
        Arrays.stream(articleListVos).forEach(this::setCommentCount);


        //转成list
        List<ArticleListVo> articleListVos1 = Arrays.stream(articleListVos).collect(Collectors.toList());

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos1,total);

        return R.successResult(articleListVoPageVo);
    }

    @Override
    public R articleAuditList(Long currentPage, Long pageSize, String userName, Integer audit) {
        Long userId=null;
        if(ObjectUtil.isNotNull(userName)){
            UserEntity user_name = userService.query().eq("user_name", userName).one();
            if(ObjectUtil.isNotNull(user_name)){
                userId=user_name.getId();
            }else {
                userId=0L;
            }
        }

        Long skipNumber =(currentPage-1) * pageSize;
        ArticleAudit[] articleAudit =baseMapper.articleAuditList(skipNumber,pageSize,userId,audit);
        //获取复合条件的总数
        Long total= baseMapper.getCount();

        List<ArticleAudit> collect = Arrays.stream(articleAudit).collect(Collectors.toList());
        PageVo<ArticleAudit> articleAuditPageVo = new PageVo<>(collect, total);


        return R.successResult(articleAuditPageVo);
    }

    @Override
    public R articleRecommend(Long id, String isTop) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(id);
        articleEntity.setIsTop(isTop);
        boolean b = updateById(articleEntity);
        return R.successResult();
    }

    @Override
    public R articleAudit(Long id, Integer audit) {
        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setId(id);
        articleEntity.setAudit(audit);
        boolean b = updateById(articleEntity);
        return R.successResult();
    }

    @Override
    public R articleContent(Long id) {

        ArticleEntity article = getById(id);

        if(ObjectUtil.isNotNull(article)){
            return R.successResult(article);
        }else {
            return R.errorResult(400,"该文章不存在不存在");
        }


    }

    @Override
    public R searchArticle(Long currentPage, Long pageSize, String searchParams) {

        Long skipNumber =(currentPage-1) * pageSize;
        ArticleListVo[] articleListVos = baseMapper.searchArticleList(skipNumber,pageSize,searchParams);
        Arrays.stream(articleListVos).forEach(this::setTag);
        Arrays.stream(articleListVos).forEach(this::setCommentCount);

        //获取复合条件的总数
        Long total= baseMapper.getCount();
        //转成list
        List<ArticleListVo> articleListVos1 = Arrays.stream(articleListVos).collect(Collectors.toList());

        PageVo<ArticleListVo> articleListVoPageVo = new PageVo<>(articleListVos1,total);




        return R.successResult(articleListVoPageVo);

    }

    @Override
    @Transactional
    public R articleAndCommentCountInfo() {
        WebSiteInfo webSiteInfo = new WebSiteInfo();
        Integer articleCount = query().eq("del_flag" ,0) //获取总文章数
                .eq("status",0)
                .eq("audit",0)
                .count();
        Integer commentCount = commentService.query() //获取评论数
                .eq("del_flag" ,0)
                .eq("audit",0).count();

        webSiteInfo.setArticleCount(articleCount);
        webSiteInfo.setCommentCount(commentCount);

        return R.successResult(webSiteInfo);
    }




}
