package com.yfdl.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfdl.common.R;
import com.yfdl.service.ArticleService;
import  com.yfdl.service.FollowService;
import com.yfdl.mapper.FollowMapper;
import com.yfdl.entity.FollowEntity;
import com.yfdl.utils.SecurityUtils;
import com.yfdl.vo.follow.FollowListVo;
import com.yfdl.vo.user.AuthorInfoByArticleDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (Follow)表服务实现类
 *
 * @author makejava
 * @since 2022-11-08 22:26:51
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, FollowEntity> implements FollowService {



    @Override
    public R follow(Long followId) {

        Long userId = SecurityUtils.getUserId();

        //1.判断是否关注

        FollowEntity follow = query().eq("user_id", userId).eq("follow_user_id", followId).one();

        if (ObjectUtil.isNull(follow)){
            //3.如果没有关注，添加关注
            FollowEntity followEntity = new FollowEntity();
            followEntity.setUserId(userId);
            followEntity.setFollowUserId(followId);
            boolean save = save(followEntity);


        }else {
            //2.如果关注 ，取消关注
            boolean b = removeById(follow.getId());

        }
        //4.返回操作后的状态
        return R.successResult();
    }

    @Override
    public R followList(Long id) {
        //id 为用户id

        FollowListVo[] followList = baseMapper.getFollowList(id);


        return R.successResult(followList);
    }
}
